package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

import java.util.Date;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public class PaymentRequest extends AbstractRequest{

    private String tradeNo;

    private String payChannel;

    private Long topicId;

    private Long userId;

    private int payAmount;

    private String description;

    private Date payDate;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
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

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "tradeNo='" + tradeNo + '\'' +
                ", payChannel='" + payChannel + '\'' +
                ", userId=" + userId +
                ", payAmount=" + payAmount +
                ", description='" + description + '\'' +
                ", payDate=" + payDate +
                '}';
    }
}
