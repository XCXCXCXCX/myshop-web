package com.xcxcxcxcx.myshop.dividecenter.dto;

import java.util.Map;

/**
 * @author XCXCXCXCX
 * @date 2018/11/12
 * @comments
 */
public class PaymentNotify {

    private String tradeNo;

    private Map<String, Object> resultMap;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }
}
