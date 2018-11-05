package com.xcxcxcxcx.myshop.divide.service;

import com.alibaba.dubbo.common.logger.Logger;
import com.xcxcxcxcx.myshop.divide.util.LogEntityBuilder;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
public interface ILogSenderService {

    void synDeliver(Logger logger, LogEntityBuilder.LogEntity logEntity);

}
