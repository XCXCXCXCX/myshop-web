package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.response.AbstractResponse;

import java.util.Date;

/**
 * @author XCXCXCXCX
 * @date 2018/11/12
 * @comments
 */
public class UserQueryResponse extends AbstractResponse{

    private Long id;

    private String username;

    private String alipayNumber;

    private String wechatNumber;

    private int status;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlipayNumber() {
        return alipayNumber;
    }

    public void setAlipayNumber(String alipayNumber) {
        this.alipayNumber = alipayNumber;
    }

    public String getWechatNumber() {
        return wechatNumber;
    }

    public void setWechatNumber(String wechatNumber) {
        this.wechatNumber = wechatNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserQueryResponse{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", alipayNumber='" + alipayNumber + '\'' +
                ", wechatNumber='" + wechatNumber + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
