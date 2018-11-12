package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

/**
 * @author XCXCXCXCX
 * @date 2018/11/12
 * @comments
 */
public class AfterPayUpdateRequest extends AbstractRequest{

    private Long billId;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    @Override
    public String toString() {
        return "AfterPayUpdateRequest{" +
                "billId=" + billId +
                '}';
    }
}
