package com.xcxcxcxcx.myshop.pay.dal.persistence;

import com.xcxcxcxcx.myshop.pay.dal.entity.PayEntity;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
public interface PayMapper {

    int insertPay(PayEntity payEntity);

    int updatePayStatus(String payId, int oldStatus, int status);
}
