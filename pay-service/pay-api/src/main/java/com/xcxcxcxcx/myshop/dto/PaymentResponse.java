package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.response.AbstractResponse;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public class PaymentResponse extends AbstractResponse{

    private String tradeNo;

    private String url;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
                "tradeNo='" + tradeNo + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
