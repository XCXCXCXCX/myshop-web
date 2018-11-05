package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public class BillStatusUpdateRequest extends AbstractRequest {

    private Long billId;

    private int status;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BillStatusUpdateRequest{" +
                "billId=" + billId +
                ", status=" + status +
                '}';
    }
}
