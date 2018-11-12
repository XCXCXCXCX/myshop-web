package com.xcxcxcxcx.myshop.pay.payment.abs;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
public class PaymentContext {

    public PaymentContext() {
    }

    //订单号
    private String tradeNo;

    //总金额
    private Double totalAmount;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
