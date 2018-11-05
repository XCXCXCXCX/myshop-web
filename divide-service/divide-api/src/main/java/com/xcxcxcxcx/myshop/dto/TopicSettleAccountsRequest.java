package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
public class TopicSettleAccountsRequest extends AbstractRequest{

    private Long topicId;

    private Long durationTime;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Long durationTime) {
        this.durationTime = durationTime;
    }

    @Override
    public String toString() {
        return "TopicSettleAccountsRequest{" +
                "topicId=" + topicId +
                ", durationTime=" + durationTime +
                '}';
    }
}
