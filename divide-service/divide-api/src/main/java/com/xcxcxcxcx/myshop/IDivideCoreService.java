package com.xcxcxcxcx.myshop;

import com.xcxcxcxcx.myshop.dto.*;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public interface IDivideCoreService {

    //创建topic
    TopicCreateResponse createTopic(TopicCreateRequest request);

    //加入topic
    TopicJoinResponse joinTopic(TopicJoinRequest request);

    //查看topic by userid
    TopicQueryByUseridResponse topicQueryByUserid(TopicQueryByUseridRequest request);

    //查看topic by topicid
    TopicQueryByTopicidResponse topicQueryByTopicid(TopicQueryByTopicidRequest request);

    //查看bill by topicId and userId
    BillQueryResponse billQueryByTopicIdAndUserId(BillQueryRequest request);

}
