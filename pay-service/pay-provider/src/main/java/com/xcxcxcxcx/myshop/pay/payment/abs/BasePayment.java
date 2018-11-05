package com.xcxcxcxcx.myshop.pay.payment.abs;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;
import com.xcxcxcxcx.service.support.core.response.AbstractResponse;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
public abstract class BasePayment implements Payment{

    public static Map<String,BasePayment> paymentChannelMap = new ConcurrentHashMap<>();

    @PostConstruct
    protected void init(){
        paymentChannelMap.put(getPayChannel(),this);
    }

    //获得验证器
    protected abstract Validator getValidator();

    //获得支付渠道
    protected abstract String getPayChannel();

    protected abstract PaymentContext createContext(AbstractRequest request);

    protected abstract void requestValidate(AbstractRequest request);

    protected abstract void dataPrepare(PaymentContext context);

    protected abstract AbstractResponse doPay(PaymentContext context);

    protected abstract void afterPay(AbstractRequest request, AbstractResponse response, PaymentContext context);

    @Override
    public AbstractResponse process(AbstractRequest request) {

        //验证
        requestValidate(request);

        //创建context
        PaymentContext context = createContext(request);

        //准备
        dataPrepare(context);

        //执行
        AbstractResponse response = doPay(context);

        //善后
        afterPay(request,response,context);

        return response;
    }

}
