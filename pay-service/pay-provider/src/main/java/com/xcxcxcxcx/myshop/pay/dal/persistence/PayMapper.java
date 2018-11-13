package com.xcxcxcxcx.myshop.pay.dal.persistence;

import com.xcxcxcxcx.myshop.pay.dal.entity.PayEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
public interface PayMapper {

    int insertPay(@Param("payEntity") PayEntity payEntity);

    int updatePayStatus(@Param("payId") String payId,
                        @Param("oldStatus") int oldStatus,
                        @Param("status") int status);
}
