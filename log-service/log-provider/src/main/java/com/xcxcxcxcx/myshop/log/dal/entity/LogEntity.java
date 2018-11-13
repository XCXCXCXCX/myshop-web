package com.xcxcxcxcx.myshop.log.dal.entity;

import com.xcxcxcxcx.myshop.dto.LogEntityBuilder;

import java.util.Date;


/**
 * @author XCXCXCXCX
 * @date 2018/11/5
 * @comments
 */
public class LogEntity{

    private Long opId;

    private int opType;

    private Long userId;

    private Date opTime;

    private String fromApp;

    private String logInfo;

    public LogEntity(LogEntityBuilder.LogEntity logEntity) {
        this.opId = logEntity.getOpId();
        this.opType = logEntity.getOpType();
        this.userId = logEntity.getUserId();
        this.opTime = logEntity.getOpTime();
        this.fromApp = logEntity.getFromApp();
        this.logInfo = logEntity.getLogInfo();
    }

    public Long getOpId() {
        return opId;
    }

    public void setOpId(Long opId) {
        this.opId = opId;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public String getFromApp() {
        return fromApp;
    }

    public void setFromApp(String fromApp) {
        this.fromApp = fromApp;
    }

    public String getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo;
    }
}