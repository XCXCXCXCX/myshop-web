package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public class TopicCreateRequest extends AbstractRequest {

    private Double unitAmount;

    private Long publisherId;

    private Long delayBeginTime;

    private Long duration;

    public Double getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(Double unitAmount) {
        this.unitAmount = unitAmount;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
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
                ", unitAmount=" + unitAmount +
                ", publisherId=" + publisherId +
                ", delayBeginTime=" + delayBeginTime +
                ", duration=" + duration +
                '}';
    }
}
