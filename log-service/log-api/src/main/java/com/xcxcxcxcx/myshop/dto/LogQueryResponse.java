package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.response.AbstractResponse;

import java.util.List;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
public class LogQueryResponse extends AbstractResponse{

    private List<LogEntityBuilder.LogEntity> logEntities;

    public List<LogEntityBuilder.LogEntity> getLogEntities() {
        return logEntities;
    }

    public void setLogEntities(List<LogEntityBuilder.LogEntity> logEntities) {
        this.logEntities = logEntities;
    }

    @Override
    public String toString() {
        return "LogQueryResponse{" +
                "logEntities=" + logEntities.toString() +
                '}';
    }
}
