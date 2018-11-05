package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.response.AbstractResponse;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public class BillQueryResponse extends AbstractResponse{

    private Long billId;

    private Long userId;

    private int status;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BillQueryResponse{" +
                "billId=" + billId +
                ", userId=" + userId +
                ", status=" + status +
                '}';
    }
}
