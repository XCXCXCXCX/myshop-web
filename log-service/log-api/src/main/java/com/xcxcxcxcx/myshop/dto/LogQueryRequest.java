package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

import java.util.Date;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
public class LogQueryRequest extends AbstractRequest{

    private int opType;

    private Long userId;

    private Date fromTime;

    private Date toTime;

    private String fromApp;

    private String blurLogInfo;

    private int pageSize;

    private int pageNum;

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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public String toString() {
        return "LogQueryRequest{" +
                "opType=" + opType +
                ", userId=" + userId +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime +
                ", fromApp='" + fromApp + '\'' +
                ", blurLogInfo='" + blurLogInfo + '\'' +
                ", pageSize=" + pageSize +
                ", pageNum=" + pageNum +
                '}';
    }
}
