package com.xcxcxcxcx.myshop.log.dal.persistence;

import com.xcxcxcxcx.myshop.log.dal.dto.LogQuery;
import com.xcxcxcxcx.myshop.log.dal.entity.LogEntity;

import java.util.List;


/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
public interface LogMapper {

    //insert log
    int insertLog(LogEntity logEntity);

    //query log
    List<LogEntity> queryLog(LogQuery logQuery);

    //clean log
    int deleteLog(Long expiredValue);

}
