package com.xcxcxcxcx.myshop.user.dal.entity;

import java.util.Date;

/**
 * @author XCXCXCXCX
 * @date 2018/10/30
 * @comments
 */
public class User {

    private Long id;

    private String username;

    private String password;

    private String alipayNumber;

    private String wechatNumber;

    private Long lastLogid;

    private int status;

    private Date createTime;

    private int versionNumber;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Long getLastLogid() {
        return lastLogid;
    }

    public void setLastLogid(Long lastLogid) {
        this.lastLogid = lastLogid;
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

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
}
