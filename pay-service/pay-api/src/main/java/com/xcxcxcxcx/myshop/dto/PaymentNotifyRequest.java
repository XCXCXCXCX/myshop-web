package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

import java.util.Map;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public class PaymentNotifyRequest extends AbstractRequest{

    private String payChannel;

    private Map<String,Object> resultMap; //服务端返回的支付通知结果

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    @Override
    public String toString() {
        return "PaymentNotifyRequest{" +
                "payChannel='" + payChannel + '\'' +
                ", resultMap=" + resultMap +
                '}';
    }
}
