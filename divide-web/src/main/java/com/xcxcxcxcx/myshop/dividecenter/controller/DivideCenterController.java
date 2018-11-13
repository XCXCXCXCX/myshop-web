package com.xcxcxcxcx.myshop.dividecenter.controller;

import com.xcxcxcxcx.common.BaseController;
import com.xcxcxcxcx.common.annotation.Anoymous;
import com.xcxcxcxcx.myshop.*;
import com.xcxcxcxcx.myshop.constants.ResponseCodeEnum;
import com.xcxcxcxcx.myshop.dividecenter.dto.Payment;
import com.xcxcxcxcx.myshop.dividecenter.dto.PaymentNotify;
import com.xcxcxcxcx.myshop.dividecenter.event.CreateTopicEvent;
import com.xcxcxcxcx.myshop.dividecenter.event.DivideTypeEvent;
import com.xcxcxcxcx.myshop.dto.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author XCXCXCXCX
 * @date 2018/11/12
 * @comments
 */
@RequestMapping("/divide")
@RestController
public class DivideCenterController extends BaseController implements ApplicationEventPublisherAware {

    private static final Logger logger = LogManager.getLogger(DivideCenterController.class);

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

    @Autowired
    IDivideCoreService divideCoreService;

    @Autowired
    IDivideGrabService divideGrabService;

    @Autowired
    IPayCoreService payCoreService;

    @Autowired
    ILogCollectService logCollectService;

    @Autowired
    IUserCoreSerivce userCoreSerivce;

    private ApplicationEventPublisher publisher;

    /**
     * 创建红包群
     */
    @Anoymous
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity createTopic(@RequestParam("unitAmount") Double unitAmount,
                                      @RequestParam("publisherId")Long publisherId,
                                      @RequestParam("delayBeginTime")Long delayBeginTime,
                                      @RequestParam("duration")Long duration){

        TopicCreateRequest topicCreateRequest = new TopicCreateRequest();
        topicCreateRequest.setUnitAmount(unitAmount);
        topicCreateRequest.setPublisherId(publisherId);
        topicCreateRequest.setDelayBeginTime(delayBeginTime);
        topicCreateRequest.setDuration(duration);
        TopicCreateResponse response = divideCoreService.createTopic(topicCreateRequest);

        if("00000000".equals(response.getCode())){
            CreateTopicEvent createTopicEvent = new CreateTopicEvent("DivideCenterController",
                    response.getTopicId(), delayBeginTime, duration);
            publisher.publishEvent(createTopicEvent);
            logger.debug("发布事件 : " + createTopicEvent.toString());
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 加入红包群
     */
    @Anoymous
    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity joinTopic(@RequestParam("topicId") Long topicId,
                                    @RequestParam("userId") Long userId){

        TopicJoinRequest request = new TopicJoinRequest();
        request.setTopicId(topicId);
        request.setUserId(userId);

        TopicJoinResponse response = divideCoreService.joinTopic(request);

        if("00000000".equals(response.getCode())){
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 支付
     */
    @Anoymous
    @PostMapping("/prePay")
    @ResponseBody
    public ResponseEntity prePayTopic(@RequestBody Payment payment){

        PaymentRequest request = new PaymentRequest();

        request.setTradeNo(UUID.randomUUID().toString());
        request.setTopicId(payment.getTopicId());
        request.setUserId(payment.getUserId());
        request.setPayDate(new Date());
        request.setDescription("支付瓜分红包");
        request.setPayAmount(payment.getPayAmount());
        request.setPayChannel(payment.getPayChannel());
        PaymentResponse response = payCoreService.prePay(request);

        if("00000000".equals(response.getCode())){
            Map<String,Object> resultMap = new HashMap();
            resultMap.put("tradeNo", response.getTradeNo());
            resultMap.put("url", response.getUrl());
            return ResponseEntity.ok(resultMap);
        }

        return ResponseEntity.badRequest().body(response.getMsg());
    }

    /**
     * 支付回调,需要满足幂等
     */
    @Anoymous
    @PostMapping("/payCallback")
    @ResponseBody
    public ResponseEntity payCallback(@RequestBody PaymentNotify paymentNotify){
        PaymentNotifyRequest request = new PaymentNotifyRequest();
        request.setPayChannel(paymentNotify.getTradeNo());
        request.setResultMap(paymentNotify.getResultMap());
        PaymentNotifyResponse response = payCoreService.payCallback(request);

        if("00000000".equals(response.getCode())){
            return ResponseEntity.ok().build();
        }

        logger.error("支付回调异常: " + response.getMsg());
        //日志服务

        return ResponseEntity.badRequest().build();
    }

    /**
     * 转账回调,需要满足幂等
     */
    @Anoymous
    @PostMapping("/transferCallback")
    @ResponseBody
    public ResponseEntity transferCallback(@RequestBody PaymentNotify paymentNotify){
        PaymentNotifyRequest request = new PaymentNotifyRequest();
        request.setPayChannel(paymentNotify.getTradeNo());
        request.setResultMap(paymentNotify.getResultMap());
        PaymentNotifyResponse response = payCoreService.transferNotify(request);

        if("00000000".equals(response.getCode())){
            return ResponseEntity.ok().build();
        }

        logger.error("支付回调异常: " + response.getMsg());
        //日志服务

        return ResponseEntity.badRequest().build();
    }

    /**
     * 抢红包
     */
    @Anoymous
    @PostMapping("/grab")
    @ResponseBody
    public ResponseEntity grabTopic(@RequestParam("userId") Long userId,
                          @RequestParam("topicId") Long topicId){
        TopicGrabRequest request = new TopicGrabRequest();
        request.setUserId(userId);
        request.setTopicId(topicId);

        TopicGrabResponse response = divideGrabService.topicGrab(request);

        if("00000000".equals(response.getCode())){
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body(response.getMsg());

    }


    /**
     *
     * @param applicationEventPublisher
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

//    @EventListener
//    public void onApplicationEvent(DivideTypeEvent event) {
//        if(event instanceof CreateTopicEvent){
//            onCreateTopicEvent((CreateTopicEvent) event);
//        }
//    }
//
//    private void onCreateTopicEvent(CreateTopicEvent event){
//        TopicPreFetchRequest request = new TopicPreFetchRequest();
//        Long topicId = event.getTopicId();
//        request.setTopicId(topicId);
//        request.setDelayTime(event.getDelayTime());
//        TopicPreFetchResponse response = divideGrabService.topicPreFetch(request);
//        if(ResponseCodeEnum.SUCCESS.getCode().equals(response.getCode())){
//            logger.info("preFetch任务投递成功并投递对账任务");
//            scheduledExecutorService.schedule(()->{
//                settleAccount(topicId);
//            },event.getDelayTime() + event.getDuration(), TimeUnit.SECONDS);
//            return;
//        }
//
//        String msg = "preFetch任务投递失败: " + event.toString();
//        logger.error(msg);
//        //日志服务
//        LogDeliverRequest logDeliverRequest = new LogDeliverRequest();
//        logDeliverRequest.setLogEntity(LogEntityBuilder.createLog(LogEntityBuilder.OpTypeEnum.ERROR,
//                0L,
//                msg).build());
//        logCollectService.deliverLog(logDeliverRequest);
//        if("00000000".equals(response.getCode())){
//            logger.debug("投递日志成功");
//        }else{
//            logger.error("投递日志失败: " + logDeliverRequest.toString());
//        }
//
//    }
//
//    /**
//     * 对账
//     * @param topicId
//     */
//    private void settleAccount(Long topicId){
//        TopicSettleAccountsRequest request = new TopicSettleAccountsRequest();
//        request.setTopicId(topicId);
//        TopicSettleAccountsResponse response = divideGrabService.topicSettleAccounts(request);
//        if(ResponseCodeEnum.SUCCESS.getCode().equals(response.getCode())){
//            //对账成功
//            //给该topic下的用户转账
//            doBatchTransfer(topicId);
//            return;
//        }
//
//        //对账失败
//        //日志服务
//        LogDeliverRequest logDeliverRequest = new LogDeliverRequest();
//        logDeliverRequest.setLogEntity(LogEntityBuilder.createLog(LogEntityBuilder.OpTypeEnum.ERROR,
//                0L,
//                "topicId="+topicId+"->对账失败").build());
//        logCollectService.deliverLog(logDeliverRequest);
//        if("00000000".equals(response.getCode())){
//            logger.debug("投递日志成功");
//        }else{
//            logger.error("投递日志失败: " + logDeliverRequest.toString());
//        }
//    }
//
//    /**
//     * 批量转账，红包结算
//     * @param topicId
//     */
//    private void doBatchTransfer(Long topicId) {
//        //从topicId 获取bill
//        TopicQueryByTopicidRequest request = new TopicQueryByTopicidRequest();
//        request.setTopicId(topicId);
//        TopicQueryByTopicidResponse response = divideCoreService.topicQueryByTopicid(request);
//        List<BillEntity> billEntityList = response.getBillList();
//        //从bill 获取userid,transferAmount
//        for(BillEntity x: billEntityList) {
//
//            PaymentTransferRequest transferRequest = new PaymentTransferRequest();
//            Long userId = x.getUserId();
//            transferRequest.setTradeNo(UUID.fromString(x.toString()).toString());
//            transferRequest.setUserId(userId);
//            transferRequest.setTopicId(topicId);
//            transferRequest.setTransferAmount(x.getCurrentAmount() - response.getUnitAmount());
//            UserQueryRequest userQueryRequest = new UserQueryRequest();
//            userQueryRequest.setUserId(userId);
//            UserQueryResponse userQueryResponse = userCoreSerivce.queryUserByUserid(userQueryRequest);
//            String userAccount = userQueryResponse.getWechatNumber() == null ?
//                    userQueryResponse.getAlipayNumber() : userQueryResponse.getWechatNumber();
//            transferRequest.setUserAccount(userAccount);
//
//            PaymentTransferResponse transferResponse = payCoreService.transfer(transferRequest);
//            if ("00000000".equals(transferResponse.getCode())) {
//                logger.info("trade = " + transferResponse.getTradeNo() + "->发起转账成功");
//                return;
//            }
//
//            String msg = "trade = " + transferResponse.getTradeNo() + "->发起转账失败";
//            logger.error(msg);
//            LogDeliverRequest logDeliverRequest = new LogDeliverRequest();
//            logDeliverRequest.setLogEntity(LogEntityBuilder.createLog(LogEntityBuilder.OpTypeEnum.ERROR,
//                    0L,
//                    msg).build());
//            LogDeliverResponse logDeliverResponse = logCollectService.deliverLog(logDeliverRequest);
//            if ("00000000".equals(logDeliverResponse.getCode())) {
//                logger.debug("投递日志成功");
//            } else {
//                logger.error("投递日志失败: " + logDeliverRequest.toString());
//            }
//
//        }
//
//    }

}
