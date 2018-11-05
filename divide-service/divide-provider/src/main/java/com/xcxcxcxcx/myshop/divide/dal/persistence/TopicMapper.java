package com.xcxcxcxcx.myshop.divide.dal.persistence;

import com.xcxcxcxcx.myshop.divide.dal.entity.Topic;

import java.util.List;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public interface TopicMapper {

    int insertTopic(Topic topic);

    int updateTopicStatus(Long topicId, int oldStatus, int status);

    List<Topic> getTopicByUserid(Long userId);

    Topic getTopicByTopicid(Long topicId);

    int updateTopicCurrentAmount(Long topicId, int addAmount);

}
