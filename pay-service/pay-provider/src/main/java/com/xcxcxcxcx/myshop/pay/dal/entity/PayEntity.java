package com.xcxcxcxcx.myshop.pay.dal.entity;

import java.sql.Timestamp;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
public class PayEntity {

    private String payId;

    private Long topicId;

    private Long userId;

    private Double payAmount;

    private Timestamp payDate;

    private int status;

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public Timestamp getPayDate() {
        return payDate;
    }

    public void setPayDate(Timestamp payDate) {
        this.payDate = payDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
