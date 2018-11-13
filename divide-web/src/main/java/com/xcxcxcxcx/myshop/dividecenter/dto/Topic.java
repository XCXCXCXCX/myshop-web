package com.xcxcxcxcx.myshop.dividecenter.dto;

/**
 * @author XCXCXCXCX
 * @date 2018/11/12
 * @comments
 */
public class Topic {

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

}
