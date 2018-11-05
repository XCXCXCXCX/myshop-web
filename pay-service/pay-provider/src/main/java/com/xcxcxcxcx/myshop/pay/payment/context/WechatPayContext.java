package com.xcxcxcxcx.myshop.pay.payment.context;

import com.xcxcxcxcx.myshop.pay.payment.abs.PaymentContext;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
public class WechatPayContext extends PaymentContext{

    private String body; //商品描述（必填）

    private String tradeType;//交易类型（必填）

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
}
