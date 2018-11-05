package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.response.AbstractResponse;

import java.util.List;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public class TopicQueryByUseridResponse extends AbstractResponse{

    private List<TopicEntity> topicList;

    public List<TopicEntity> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<TopicEntity> topicList) {
        this.topicList = topicList;
    }

    @Override
    public String toString() {
        return "TopicQueryByUseridResponse{" +
                "topicList=" + topicList.toString() +
                '}';
    }
}
