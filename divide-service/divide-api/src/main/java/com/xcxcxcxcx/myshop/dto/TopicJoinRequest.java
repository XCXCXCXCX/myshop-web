package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public class TopicJoinRequest extends AbstractRequest{

    private Long userId;

    private Long topicId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    @Override
    public String toString() {
        return "TopicJoinRequest{" +
                "userId=" + userId +
                ", topicId=" + topicId +
                '}';
    }
}
