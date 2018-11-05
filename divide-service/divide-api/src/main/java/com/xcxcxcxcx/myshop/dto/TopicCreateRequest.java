package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public class TopicCreateRequest extends AbstractRequest {

    private Long topicId;

    private int unitAmount;

    private Long publisherId;

    private int status;

    private Long delayBeginTime;

    private Long duration;

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

    public Long getDelayBeginTime() {
        return delayBeginTime;
    }

    public void setDelayBeginTime(Long delayBeginTime) {
        this.delayBeginTime = delayBeginTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "TopicCreateRequest{" +
                "topicId=" + topicId +
                ", unitAmount=" + unitAmount +
                ", publisherId=" + publisherId +
                ", status=" + status +
                ", delayBeginTime=" + delayBeginTime +
                ", duration=" + duration +
                '}';
    }
}
