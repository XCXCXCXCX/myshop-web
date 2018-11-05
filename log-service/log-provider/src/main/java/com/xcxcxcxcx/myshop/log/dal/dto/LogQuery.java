package com.xcxcxcxcx.myshop.log.dal.dto;

import java.util.Date;

/**
 * @author XCXCXCXCX
 * @date 2018/11/5
 * @comments
 */
public class LogQuery{

    private int opType;

    private Long userId;

    private Date fromTime;

    private Date toTime;

    private String fromApp;

    private String blurLogInfo;

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

    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public Date getToTime() {
        return toTime;
    }

    public void setToTime(Date toTime) {
        this.toTime = toTime;
    }

    public String getFromApp() {
        return fromApp;
    }

    public void setFromApp(String fromApp) {
        this.fromApp = fromApp;
    }

    public String getBlurLogInfo() {
        return blurLogInfo;
    }

    public void setBlurLogInfo(String blurLogInfo) {
        this.blurLogInfo = blurLogInfo;
    }

}
