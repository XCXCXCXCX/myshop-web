package com.xcxcxcxcx.myshop.divide.dal.entity;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public class Bill {

    private Long billId;

    private Long topicId;

    private Long userId;

    private int status;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", topicId=" + topicId +
                ", userId=" + userId +
                ", status=" + status +
                '}';
    }
}
