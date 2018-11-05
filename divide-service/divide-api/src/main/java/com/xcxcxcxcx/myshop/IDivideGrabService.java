package com.xcxcxcxcx.myshop;

import com.xcxcxcxcx.myshop.dto.*;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
public interface IDivideGrabService {

    //预置红包:从mysql拉取到redis
    TopicPreFetchResponse topicPreFetch(TopicPreFetchRequest request);

    //抢红包:从redis中抢
    TopicGrabResponse topicGrab(TopicGrabRequest request);

    //红包销毁:令redis数据写入到mysql并在redis失效
    TopicSettleAccountsResponse topicSettleAccounts(TopicSettleAccountsRequest request);

}
