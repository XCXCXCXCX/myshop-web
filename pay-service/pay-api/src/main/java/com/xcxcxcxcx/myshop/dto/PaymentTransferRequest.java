package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
public class PaymentTransferRequest extends AbstractRequest{

    private Long topicId;

    private String tradeNo;

    private Long userId;

    private String userAccount;

    private int transferAmount;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public int getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(int transferAmount) {
        this.transferAmount = transferAmount;
    }

    @Override
    public String toString() {
        return "PaymentTransferRequest{" +
                "topicId=" + topicId +
                ", tradeNo='" + tradeNo + '\'' +
                ", userId=" + userId +
                ", userAccount='" + userAccount + '\'' +
                ", transferAmount=" + transferAmount +
                '}';
    }
}
