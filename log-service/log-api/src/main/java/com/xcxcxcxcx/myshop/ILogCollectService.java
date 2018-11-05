package com.xcxcxcxcx.myshop;

import com.xcxcxcxcx.myshop.dto.*;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
public interface ILogCollectService {

    //投递log消息(分同步和异步)
    LogDeliverResponse deliverLog(LogDeliverRequest request);

    //查询log记录
    LogQueryResponse queryLog(LogQueryRequest request);

    //clean expire log
    LogCleanResponse cleanLog(LogCleanRequest request);

}
