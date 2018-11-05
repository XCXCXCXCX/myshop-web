package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public class TopicStatusUpdateRequest extends AbstractRequest {

    private Long topicId;

    private int status;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TopicStatusUpdateRequest{" +
                "topicId=" + topicId +
                ", status=" + status +
                '}';
    }
}
