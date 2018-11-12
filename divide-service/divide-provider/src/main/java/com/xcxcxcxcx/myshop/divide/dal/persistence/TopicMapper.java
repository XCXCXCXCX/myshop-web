package com.xcxcxcxcx.myshop.divide.dal.persistence;

import com.xcxcxcxcx.myshop.divide.dal.entity.Topic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public interface TopicMapper {

    int insertTopic(Topic topic);

    int updateTopicStatus(@Param("topicId") Long topicId,
                          @Param("oldStatus") int oldStatus,
                          @Param("status") int status);

    List<Topic> getTopicByUserid(Long userId);

    Topic getTopicByTopicid(Long topicId);

}
