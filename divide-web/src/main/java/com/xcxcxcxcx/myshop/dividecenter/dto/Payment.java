package com.xcxcxcxcx.myshop.dividecenter.dto;

/**
 * @author XCXCXCXCX
 * @date 2018/11/12
 * @comments
 */
public class Payment {

    private Long topicId;

    private Long userId;

    private Double payAmount;

    private String payChannel;

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

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "-"+topicId +
                "-"+userId +
                "-"+payAmount +
                "-"+payChannel +
                "}";
    }
}
