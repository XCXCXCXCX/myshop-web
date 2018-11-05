package com.xcxcxcxcx.myshop.pay.service;

import com.alibaba.dubbo.common.logger.Logger;
import com.xcxcxcxcx.myshop.pay.util.LogEntityBuilder;
import com.xcxcxcxcx.service.support.core.response.AbstractResponse;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
public interface ILogSenderService {

    void synDeliver(Logger logger, LogEntityBuilder.LogEntity logEntity, AbstractResponse response);

}
