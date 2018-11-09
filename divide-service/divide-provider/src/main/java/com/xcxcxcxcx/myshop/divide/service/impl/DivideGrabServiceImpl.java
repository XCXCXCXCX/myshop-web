package com.xcxcxcxcx.myshop.divide.service.impl;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.xcxcxcxcx.myshop.IDivideGrabService;
import com.xcxcxcxcx.myshop.constants.ResponseCodeEnum;
import com.xcxcxcxcx.myshop.divide.dal.entity.Bill;
import com.xcxcxcxcx.myshop.divide.dal.entity.Topic;
import com.xcxcxcxcx.myshop.divide.dal.persistence.BillMapper;
import com.xcxcxcxcx.myshop.divide.dal.persistence.TopicMapper;
import com.xcxcxcxcx.myshop.divide.dal.persistence.TopicStatusEnum;
import com.xcxcxcxcx.myshop.divide.exception.ServiceException;
import com.xcxcxcxcx.myshop.divide.exception.ValidateException;
import com.xcxcxcxcx.myshop.divide.service.ILogSenderService;
import com.xcxcxcxcx.myshop.divide.service.IPersistentTransactionService;
import com.xcxcxcxcx.myshop.divide.util.ExceptionUtils;
import com.xcxcxcxcx.myshop.divide.util.LogEntityBuilder;
import com.xcxcxcxcx.myshop.dto.*;
import com.xcxcxcxcx.service.support.core.request.AbstractRequest;
import org.apache.kafka.clients.producer.internals.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
@Service("divideGrabService")
public class DivideGrabServiceImpl implements IDivideGrabService {

    private static final Logger logger = LoggerFactory.getLogger(DivideGrabServiceImpl.class);

    private final ScheduledExecutorService delayExecutor = Executors.newScheduledThreadPool(10);

    private final ExecutorService asynPersistentExecutor = Executors.newFixedThreadPool(10);

    private final Random random = new Random();

    @Autowired
    TopicMapper topicMapper;

    @Autowired
    BillMapper billMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ILogSenderService logSenderService;

    @Autowired
    IPersistentTransactionService persistentTransactionService;

    public TopicPreFetchResponse topicPreFetch(TopicPreFetchRequest topicPreFetchRequest) {

        TopicPreFetchResponse response = new TopicPreFetchResponse();

        try {
            //验证
            validateRequest(topicPreFetchRequest);

            //提交延迟任务，延迟delayTime
            delayExecutor.schedule(() -> {

                Long topicId = topicPreFetchRequest.getTopicId();

                //延迟任务：
                //1.检查mysql中是否存在该topic并检查该topic状态
                Topic topic = topicMapper.getTopicByTopicid(topicId);
                if (topic == null) {
                    //日志记录
                    logger.error("topicId = " + topicId + "-> 不存在该topic，无法preFetch");
                    //日志记录
                    LogEntityBuilder.LogEntity logEntity =
                            LogEntityBuilder.createLog(LogEntityBuilder.OpTypeEnum.ERROR
                                    , 0L
                                    , "topicId = " + topicId + "-> preFetch失败，请重试,cause of : 不存在该topic，无法preFetch").build();
                    logSenderService.synDeliver(logger, logEntity);
                    return;
                }
                if (topic.getStatus() != 0) {
                    //日志记录
                    logger.error("topicId = " + topicId + "-> topic状态有误，无法preFetch");
                    LogEntityBuilder.LogEntity logEntity =
                            LogEntityBuilder.createLog(LogEntityBuilder.OpTypeEnum.ERROR
                                    , 0L
                                    , "topicId = " + topicId + "-> preFetch失败，请重试,cause of : topic状态有误，无法preFetch").build();
                    logSenderService.synDeliver(logger, logEntity);
                    return;
                }

                //2.查询该topic关联的billId，根据查询到的bill，封装后写入redis
                List<Bill> billList = billMapper.getBillByTopicid(topicId);

                int size = 0;
                List<Bill> legalBillList = null;
                if (billList != null) {
                    legalBillList = billList.stream().filter(bill ->
                            bill.getStatus() == 1
                    ).collect(Collectors.toList());
                    size = legalBillList.size();
                }

//                int size = 0;
//                for(Bill bill : billList){
//                    //记录支付成功的bill
//                    if(bill.getStatus() == 1){
//                        size++;
//                    }
//                }
                try {
                    //初始化前del
                    redisTemplate.delete(topicId);
                    redisTemplate.delete(topicId + "_set");

                    double sumAmount = topic.getUnitAmount() * size;
                    for (int i = 0; i < size; i++) {
                        sumAmount = sumAmount - getCurrentRandomAmount(sumAmount, i, size - 1);
                        redisTemplate.opsForList().leftPush(topicId, String.valueOf(sumAmount));
                        redisTemplate.opsForSet().add(topicId + "_set", legalBillList.get(i));
                    }
                } catch (Exception e) {
                    logger.error("topicId = " + topicId + "-> preFetch失败，请重试,cause of :" + e);
                    //日志记录
                    LogEntityBuilder.LogEntity logEntity =
                            LogEntityBuilder.createLog(LogEntityBuilder.OpTypeEnum.ERROR
                                    , 0L
                                    , "topicId = " + topicId + "-> preFetch失败，请重试,cause of :" + e).build();
                    logSenderService.synDeliver(logger, logEntity);
                    return;
                }

                //3.更新mysql中topic的status = 2 进入抢购状态
                int row = topicMapper.updateTopicStatus(topicId, TopicStatusEnum.PREPARE.getCode(), TopicStatusEnum.DOING.getCode());
                if (row < 1) {
                    logger.error("topicId = " + topicId + " preFetch成功但mysql更新状态失败");
                    //日志记录
                    LogEntityBuilder.LogEntity logEntity =
                            LogEntityBuilder.createLog(LogEntityBuilder.OpTypeEnum.ERROR
                                    , 0L
                                    , "topicId = " + topicId + " preFetch成功但mysql更新状态失败").build();
                    logSenderService.synDeliver(logger, logEntity);
                    return;
                }

                //4.成功并记录日志
                //日志记录
                logger.info("topicId = " + topicId + "-> preFetch成功");
                LogEntityBuilder.LogEntity logEntity =
                        LogEntityBuilder.createLog(LogEntityBuilder.OpTypeEnum.ERROR
                                , 0L
                                , "topicId = " + topicId + "-> preFetch成功").build();
                logSenderService.synDeliver(logger, logEntity);

            }, topicPreFetchRequest.getDelayTime(), TimeUnit.SECONDS);

            response.setCode(ResponseCodeEnum.SUCCESS.getCode());
            response.setMsg("preFetch任务投递成功");

        } catch (Exception e) {
            logger.error("topicPreFetch occur exception : " + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        } finally {
            logger.info("topicPreFetch response : " + response.toString());
        }

        return response;
    }

    private double getCurrentRandomAmount(double sum, int i, int size) {

        if (i == size) {
            return sum;
        }

        //随机取当前总数的[10%,90%)，作为抢到的红包
        int rate = random.nextInt(80) + 10;

        BigDecimal bd = new BigDecimal((sum * rate) / 100);

        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    public TopicGrabResponse topicGrab(TopicGrabRequest topicGrabRequest) {

        TopicGrabResponse response = new TopicGrabResponse();

        try {

            validateRequest(topicGrabRequest);

            Long topicId = topicGrabRequest.getTopicId();
            Long userId = topicGrabRequest.getUserId();

            Bill bill = billMapper.getBillByTopicidAndUserid(topicId, userId);

            if (bill == null) {
                throw new ValidateException("topicGrab异常，topic中不存在该用户记录!");
            }

            Topic topic = topicMapper.getTopicByTopicid(topicId);
            if(topic.getStatus() != TopicStatusEnum.DOING.getCode()){

                throw new ValidateException("该topic目前不在可抢状态!");
            }

            //1.用户是否在红包群中,每个用户只能抢购一次
            Long row = redisTemplate.opsForSet().remove(topicId, topicGrabRequest.getUserId());
            if (row == 0) {
                throw new ValidateException("红包已抢光!");
            }

            //2.从redis pop领取红包金额
            Object userAmount = redisTemplate.opsForList().rightPop(topicId);
            if (userAmount == null) {
                throw new ValidateException("红包已抢光!");
            }

            //3.异步持久化到数据库
            //1).更新topic的current_amount
            //2).更新topic下的bill的status
            asynPersistentExecutor.submit(() -> {

                try {
                    persistentTransactionService.updateBillAmountAndStatus(
                            bill.getBillId(),
                            Integer.parseInt(userAmount.toString()),
                            0,
                            1);
                } catch (Exception e) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    //日志记录
                    logger.error("{" + topicGrabRequest.toString() + "}" + "-> grab fail cause of : 抢购成功但数据库持久化失败{" + e + "}");
                    LogEntityBuilder.LogEntity logEntity =
                            LogEntityBuilder.createLog(LogEntityBuilder.OpTypeEnum.ERROR
                                    , userId
                                    , "{" + topicGrabRequest.toString() + "}" + "-> grab fail cause of : 抢购成功但数据库持久化失败{" + e + "}").build();
                    logSenderService.synDeliver(logger, logEntity);
                }

            });

        } catch (Exception e) {
            logger.error("topicGrab occur exception : " + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        } finally {
            logger.info("topicGrab response : " + response.toString());
        }

        return response;
    }

    public TopicSettleAccountsResponse topicSettleAccounts(TopicSettleAccountsRequest topicSettleAccountsRequest) {

        TopicSettleAccountsResponse response = new TopicSettleAccountsResponse();

        try {

            validateRequest(topicSettleAccountsRequest);

            delayExecutor.schedule(() ->
                            doSettleAccounts(topicSettleAccountsRequest),
                    topicSettleAccountsRequest.getDurationTime(),
                    TimeUnit.SECONDS);

        } catch (Exception e) {
            logger.error("topicSettleAccounts occur exception : " + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        } finally {
            logger.info("topicSettleAccounts response : " + response.toString());
        }

        return response;
    }

    private void doSettleAccounts(TopicSettleAccountsRequest topicSettleAccountsRequest) {
        //对账
        Long topicId = topicSettleAccountsRequest.getTopicId();


        //从mysql中读取数据
        List<Bill> billList = billMapper.getBillByTopicid(topicId);
        List<Bill> mysqlNeedToPayBillList = billList.stream()
                .filter(x -> x.getStatus() == 0)
                .collect(Collectors.toList());

        //从redis中读取数据
        List<String> amountList = redisTemplate.opsForList().range(topicId, 0, -1);
        Set<String> userSet = redisTemplate.opsForSet().members(topicId + "_set");
        int mysqlNeedToPayNum = mysqlNeedToPayBillList.size();


        //处理redis未操作数据(以userSet为准,此时可能存在未消费的amount)
        handleRedisData(userSet, amountList, billList);

        double currentAmount = billList.stream()
                .map(x -> x.getCurrentAmount())
                .reduce(0.00, (x, y) -> x + y);

        double remainUnitAmount = currentAmount / mysqlNeedToPayNum;


        //根据mysql现有数据进行最终修正(包含redis中可能未消费的amount一并处理)
        for(Bill bill : mysqlNeedToPayBillList){

            Long billId = bill.getBillId();

            billMapper.updateBillCurrentAmount(billId, remainUnitAmount);

            billMapper.updateBillStatus(billId, 0, 1);

        }

        //最终对账
        List<Bill> newBillList = billMapper.getBillByTopicid(topicId);
        int current = newBillList.size();
        int currentSuccess = newBillList
                .stream()
                .filter(x -> x.getStatus() == 1)
                .collect(Collectors.toList())
                .size();

        if (current == currentSuccess) {

            //对账成功，令缓存失效
            redisTemplate.delete(topicId);
            redisTemplate.delete(topicId + "_set");

        } else {

            logger.error("对账有误，result : current = " + current + " but currentSuccess = " + currentSuccess);
            LogEntityBuilder.LogEntity logEntity = LogEntityBuilder.createLog(LogEntityBuilder.OpTypeEnum.ERROR,
                    0L, "对账有误，result : current = " + current + " but currentSuccess = " + currentSuccess).build();
            logSenderService.synDeliver(logger, logEntity);

        }

        //topic结束
        topicMapper.updateTopicStatus(topicId, TopicStatusEnum.DOING.getCode(), TopicStatusEnum.END.getCode());

    }

    private void handleRedisData(Set<String> userSet, List<String> amountList, List<Bill> billList) {
        int i = 0;
        for (String user : userSet) {
            Long userId = Long.valueOf(user);
            userSet.remove(user);
            double amount = Double.valueOf(amountList.get(i));
            amountList.remove(i);

            billMapper.updateBillCurrentAmount(userId, amount);

            List<Long> bills = billList.stream()
                    .filter(x -> x.getUserId() == userId)
                    .map(x -> x.getUserId())
                    .collect(Collectors.toList());
            if(bills == null || bills.size() != 1){
                logger.debug("处理redis数据时，在mysql获取该用户对应bill出错 : " + bills.toString() );
                throw new ValidateException("对账出错,reason => 处理redis数据时，在mysql获取该用户对应bill出错 : " + bills.toString());
            }
            billMapper.updateBillStatus(bills.get(0), 0, 1);

            i++;
        }
    }

    private void validateRequest(AbstractRequest request) {

        if (request == null) {
            throw new ValidateException("请求对象为空");
        }

        if (request instanceof TopicPreFetchRequest) {
            if (StringUtils.isEmpty(((TopicPreFetchRequest) request).getTopicId())) {
                throw new ValidateException("topicId为空");
            }
            if (StringUtils.isEmpty(((TopicPreFetchRequest) request).getDelayTime())) {
                throw new ValidateException("delayTime为空");
            }
        }

        if (request instanceof TopicGrabRequest) {
            if (StringUtils.isEmpty(((TopicGrabRequest) request).getTopicId())) {
                throw new ValidateException("topicId为空");
            }
            if (StringUtils.isEmpty(((TopicGrabRequest) request).getUserId())) {
                throw new ValidateException("userId为空");
            }
        }

        if (request instanceof TopicSettleAccountsRequest) {
            if (StringUtils.isEmpty(((TopicSettleAccountsRequest) request).getTopicId())) {
                throw new ValidateException("topicId为空");
            }
            if (StringUtils.isEmpty(((TopicSettleAccountsRequest) request).getDurationTime())) {
                throw new ValidateException("durationTime为空");
            }
        }

    }

}
