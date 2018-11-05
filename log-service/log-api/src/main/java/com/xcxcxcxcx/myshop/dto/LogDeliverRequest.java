package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
public class LogDeliverRequest extends AbstractRequest{

    private LogEntityBuilder.LogEntity logEntity;

    public LogEntityBuilder.LogEntity getLogEntity() {
        return logEntity;
    }

    public void setLogEntity(LogEntityBuilder.LogEntity logEntity) {
        this.logEntity = logEntity;
    }

    @Override
    public String toString() {
        return "LogDeliverRequest{" +
                "logEntity=" + logEntity +
                '}';
    }
}
