package com.xcxcxcxcx.myshop.user.dal.persistence;

import com.xcxcxcxcx.myshop.user.dal.entity.User;

/**
 * @author XCXCXCXCX
 * @date 2018/10/30
 * @comments
 */
public interface UserMapper {

    User getUserByUsername(String username);

    User getUserByUserId(Long userId);

    int insertSelective(User user);


}
