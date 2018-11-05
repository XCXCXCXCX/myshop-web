package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.response.AbstractResponse;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
public class LogCleanResponse extends AbstractResponse{

    private int delRow;

    public int getDelRow() {
        return delRow;
    }

    public void setDelRow(int delRow) {
        this.delRow = delRow;
    }
}
