package com.xcxcxcxcx.myshop.dividecenter.event.listener;


import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.xcxcxcxcx.myshop.*;
import com.xcxcxcxcx.myshop.constants.ResponseCodeEnum;
import com.xcxcxcxcx.myshop.dividecenter.event.CreateTopicEvent;
import com.xcxcxcxcx.myshop.dividecenter.event.DivideTypeEvent;
import com.xcxcxcxcx.myshop.dto.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author XCXCXCXCX
 * @date 2018/11/12
 * @comments
 */
@Component
public class DivideOpListener implements ApplicationListener<DivideTypeEvent>{

    @Override
    public void onApplicationEvent(DivideTypeEvent event) {
        if(event instanceof CreateTopicEvent){
            onCreateTopicEvent((CreateTopicEvent) event);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(DivideOpListener.class);

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

    @Autowired
    private IPayCoreService payCoreService;

    @Autowired
    private IDivideGrabService divideGrabService;

    @Autowired
    private IDivideCoreService divideCoreService;

    @Autowired
    private IUserCoreSerivce userCoreSerivce;

    @Autowired
    private ILogCollectService logCollectService;

    private void onCreateTopicEvent(CreateTopicEvent event){
        TopicPreFetchRequest request = new TopicPreFetchRequest();
        Long topicId = event.getTopicId();
        request.setTopicId(topicId);
        request.setDelayTime(event.getDelayTime());
        TopicPreFetchResponse response = divideGrabService.topicPreFetch(request);
        if(ResponseCodeEnum.SUCCESS.getCode().equals(response.getCode())){
            logger.info("preFetch任务投递成功并投递对账任务");
            scheduledExecutorService.schedule(()->{
                settleAccount(topicId);
            },event.getDelayTime() + event.getDuration(), TimeUnit.MILLISECONDS);
            return;
        }

        String msg = "preFetch任务投递失败: " + event.toString();
        logger.error(msg);
        //日志服务
        LogDeliverRequest logDeliverRequest = new LogDeliverRequest();
        logDeliverRequest.setLogEntity(LogEntityBuilder.createLog(LogEntityBuilder.OpTypeEnum.ERROR,
                0L,
                msg).build());
        logCollectService.deliverLog(logDeliverRequest);
        if("00000000".equals(response.getCode())){
            logger.debug("投递日志成功");
        }else{
            logger.error("投递日志失败: " + logDeliverRequest.toString());
        }

    }

    /**
     * 对账
     * @param topicId
     */
    private void settleAccount(Long topicId){
        TopicSettleAccountsRequest request = new TopicSettleAccountsRequest();
        request.setTopicId(topicId);
        TopicSettleAccountsResponse response = divideGrabService.topicSettleAccounts(request);
        if(ResponseCodeEnum.SUCCESS.getCode().equals(response.getCode())){
            //对账成功
            //给该topic下的用户转账
            doBatchTransfer(topicId);
            return;
        }

        //对账失败
        //日志服务
        LogDeliverRequest logDeliverRequest = new LogDeliverRequest();
        logDeliverRequest.setLogEntity(LogEntityBuilder.createLog(LogEntityBuilder.OpTypeEnum.ERROR,
                0L,
                "topicId="+topicId+"->对账失败").build());
        logCollectService.deliverLog(logDeliverRequest);
        if("00000000".equals(response.getCode())){
            logger.debug("投递日志成功");
        }else{
            logger.error("投递日志失败: " + logDeliverRequest.toString());
        }
    }

    /**
     * 批量转账，红包结算
     * @param topicId
     */
    private void doBatchTransfer(Long topicId) {
        //从topicId 获取bill
        TopicQueryByTopicidRequest request = new TopicQueryByTopicidRequest();
        request.setTopicId(topicId);
        TopicQueryByTopicidResponse response = divideCoreService.topicQueryByTopicid(request);
        List<BillEntity> billEntityList = response.getBillList();
        //从bill 获取userid,transferAmount
        for(BillEntity x: billEntityList) {

            PaymentTransferRequest transferRequest = new PaymentTransferRequest();
            Long userId = x.getUserId();
            transferRequest.setTradeNo(UUID.fromString(x.toString()).toString());
            transferRequest.setUserId(userId);
            transferRequest.setTopicId(topicId);
            transferRequest.setTransferAmount(x.getCurrentAmount() - response.getUnitAmount());
            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserId(userId);
            UserQueryResponse userQueryResponse = userCoreSerivce.queryUserByUserid(userQueryRequest);
            String userAccount = userQueryResponse.getWechatNumber() == null ?
                    userQueryResponse.getAlipayNumber() : userQueryResponse.getWechatNumber();
            transferRequest.setUserAccount(userAccount);

            PaymentTransferResponse transferResponse = payCoreService.transfer(transferRequest);
            if ("00000000".equals(transferResponse.getCode())) {
                logger.info("trade = " + transferResponse.getTradeNo() + "->发起转账成功");
                return;
            }

            String msg = "trade = " + transferResponse.getTradeNo() + "->发起转账失败";
            logger.error(msg);
            LogDeliverRequest logDeliverRequest = new LogDeliverRequest();
            logDeliverRequest.setLogEntity(LogEntityBuilder.createLog(LogEntityBuilder.OpTypeEnum.ERROR,
                    0L,
                    msg).build());
            LogDeliverResponse logDeliverResponse = logCollectService.deliverLog(logDeliverRequest);
            if ("00000000".equals(logDeliverResponse.getCode())) {
                logger.debug("投递日志成功");
            } else {
                logger.error("投递日志失败: " + logDeliverRequest.toString());
            }

        }

    }

}
