package com.xcxcxcxcx.myshop.divide.dal.persistence;

/**
 * @author XCXCXCXCX
 * @date 2018/11/9
 * @comments
 */
public enum BillStatusEnum {

    NEW(0,"未支付"),
    PAYED(1,"已支付"),
    OBTAINED(2,"已领取");



    private int code;

    private String description;

    BillStatusEnum(int code, String description) {
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
