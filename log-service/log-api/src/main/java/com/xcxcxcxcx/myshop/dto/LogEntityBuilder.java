package com.xcxcxcxcx.myshop.dto;


import java.io.Serializable;
import java.util.Date;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
public final class LogEntityBuilder {

    private static final String APP_NAME = "log-service";

    private OpTypeEnum opTypeEnum;

    private Long userId;

    private String logInfo;

    public LogEntityBuilder(OpTypeEnum opTypeEnum, Long userId, String logInfo) {
        this.opTypeEnum = opTypeEnum;
        this.userId = userId;
        this.logInfo = logInfo;
    }

    public static LogEntityBuilder createLog(OpTypeEnum opTypeEnum, Long userId, String logInfo){

        return new LogEntityBuilder(opTypeEnum,userId,logInfo);
    }

    public LogEntity build(){
        return new LogEntity(this.opTypeEnum,this.userId,this.logInfo);
    }

    public static enum OpTypeEnum{

        INFO(0,"INFO"),
        DEBUG(1,"DEBUG"),
        ERROR(2,"ERROR");

        private int code;

        private String msg;

        OpTypeEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class LogEntity implements Serializable{

        private Long opId;

        private int opType;

        private Long userId;

        private Date opTime;

        private String fromApp;

        private String logInfo;

        public LogEntity() {
        }

        public LogEntity(Long opId, int opType, Long userId, Date opTime, String fromApp, String logInfo) {
            this.opId = opId;
            this.opType = opType;
            this.userId = userId;
            this.opTime = opTime;
            this.fromApp = fromApp;
            this.logInfo = logInfo;
        }

        public LogEntity(OpTypeEnum opTypeEnum, Long userId, String logInfo) {
            this.opType = opTypeEnum.getCode();
            this.userId = userId;
            this.opTime = new Date();
            this.fromApp = APP_NAME;
            this.logInfo = logInfo;
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

        @Override
        public String toString() {
            return "LogEntityBuilder.LogEntity{" +
                    "opId=" + opId +
                    ", opType=" + opType +
                    ", userId=" + userId +
                    ", opTime=" + opTime +
                    ", fromApp='" + fromApp + '\'' +
                    ", logInfo='" + logInfo + '\'' +
                    "}";
        }
    }
}
