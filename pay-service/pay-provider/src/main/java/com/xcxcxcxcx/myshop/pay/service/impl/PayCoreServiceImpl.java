package com.xcxcxcxcx.myshop.pay.service.impl;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.xcxcxcxcx.myshop.IPayCoreService;
import com.xcxcxcxcx.myshop.constants.PayChannelEnum;
import com.xcxcxcxcx.myshop.dto.*;
import com.xcxcxcxcx.myshop.pay.exception.ServiceException;
import com.xcxcxcxcx.myshop.pay.payment.abs.BasePayment;
import com.xcxcxcxcx.myshop.pay.util.ExceptionUtils;
import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
public class PayCoreServiceImpl implements IPayCoreService {

    private static final Logger logger = LoggerFactory.getLogger(PayCoreServiceImpl.class);

    @Override
    public PaymentResponse prePay(PaymentRequest request) {
        PaymentResponse response = new PaymentResponse();

        try{
            response = (PaymentResponse) BasePayment.paymentChannelMap.get(request.getPayChannel())
                    .process(request);

        }catch (Exception e){
            logger.error("prePay occur exception :" + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        }finally {
            logger.info("prePay response: {"+ response +"}");
        }

        //模拟回调成功
        testSuccessCallback(request);

        return response;
    }

    /**
     * 模拟支付回调成功
     * @param request
     */
    private void testSuccessCallback(AbstractRequest request){

        String tradeNo = null;

        if(request instanceof PaymentRequest){
            tradeNo = ((PaymentRequest) request).getTradeNo();
        }

        if(request instanceof PaymentTransferRequest){
            tradeNo = ((PaymentTransferRequest) request).getTradeNo();
        }

        PaymentNotifyRequest paymentNotifyRequest = new PaymentNotifyRequest();
        paymentNotifyRequest.setPayChannel(PayChannelEnum.WECHAT_PAY_CHANNEL.getCode());
        Map<String,Object> resultParams = new HashMap<String,Object>();
        resultParams.put("tradeNo",tradeNo);
        resultParams.put("result_code","SUCCESS");
        paymentNotifyRequest.setResultMap(resultParams);
        payCallback(paymentNotifyRequest);
    }

    @Override
    public PaymentNotifyResponse payCallback(PaymentNotifyRequest request) {

        PaymentNotifyResponse response = new PaymentNotifyResponse();

        try{
            response = (PaymentNotifyResponse) BasePayment.paymentChannelMap.get(request.getPayChannel())
                    .notifyPayResult(request);

        }catch (Exception e){
            logger.error("payCallback occur exception :" + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        }finally {
            logger.info("payCallback response: {"+ response +"}");
        }

        return response;
    }

    @Override
    public PaymentTransferResponse transfer(PaymentTransferRequest request) {
        PaymentTransferResponse response = new PaymentTransferResponse();

        //转账操作
        try{
            response = (PaymentTransferResponse) BasePayment.paymentChannelMap.get(PayChannelEnum.WECHAT_TRANSFER_CHANNEL)
                    .process(request);

        }catch (Exception e){
            logger.error("transfer occur exception :" + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        }finally {
            logger.info("transfer response: {"+ response +"}");
        }

        //模拟回调成功
        testSuccessCallback(request);

        return response;
    }

    @Override
    public PaymentNotifyResponse transferNotify(PaymentNotifyRequest request) {
        PaymentNotifyResponse response = new PaymentNotifyResponse();

        //回调通知转账结果
        try{
            response = (PaymentNotifyResponse) BasePayment.paymentChannelMap.get(PayChannelEnum.WECHAT_TRANSFER_CHANNEL)
                    .notifyPayResult(request);

        }catch (Exception e){
            logger.error("transferNotify occur exception :" + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        }finally {
            logger.info("transferNotify response: {"+ response +"}");
        }

        return response;
    }
}
