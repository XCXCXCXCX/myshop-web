package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.response.AbstractResponse;

import java.util.List;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public class TopicQueryByTopicidResponse extends AbstractResponse{

    private Long topicId;

    private double unitAmount;

    private Long publisherId;

    private int status;

    private List<BillEntity> billList;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public double getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(double unitAmount) {
        this.unitAmount = unitAmount;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<BillEntity> getBillList() {
        return billList;
    }

    public void setBillList(List<BillEntity> billList) {
        this.billList = billList;
    }

    @Override
    public String toString() {
        return "TopicQueryByTopicidResponse{" +
                "topicId=" + topicId +
                ", unitAmount=" + unitAmount +
                ", publisherId=" + publisherId +
                ", status=" + status +
                ", billList=" + billList.toString() +
                '}';
    }
}
