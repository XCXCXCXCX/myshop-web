package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.response.AbstractResponse;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public class TopicQueryByTopicidResponse extends AbstractResponse{

    private Long topicId;

    private int unitAmount;

    private Long publisherId;

    private int status;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public int getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(int unitAmount) {
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

    @Override
    public String toString() {
        return "TopicQueryByTopicidResponse{" +
                "topicId=" + topicId +
                ", unitAmount=" + unitAmount +
                ", publisherId=" + publisherId +
                ", status=" + status +
                '}';
    }
}
