package com.xcxcxcxcx.myshop.divide.dal.persistence;

/**
 * @author XCXCXCXCX
 * @date 2018/11/9
 * @comments
 */
public enum  TopicStatusEnum {

    NEW(0,"允许加入"),
    PREPARE(1,"正在准备"),
    DOING(2,"正在进行"),
    END(3,"已结束");



    private int code;

    private String description;

    TopicStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
