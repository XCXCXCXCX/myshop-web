package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
public class TopicPreFetchRequest extends AbstractRequest{

    private Long topicId;

    private Long delayTime;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }

    @Override
    public String toString() {
        return "TopicPreFetchRequest{" +
                "topicId=" + topicId +
                ", delayTime=" + delayTime +
                '}';
    }
}
