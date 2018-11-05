package com.xcxcxcxcx.myshop;

import com.xcxcxcxcx.myshop.dto.*;

/**
 * @author XCXCXCXCX
 * @date 2018/10/21
 * @comments
 */
public interface IUserCoreSerivce {

    UserLoginResponse login(UserLoginRequest request);

    UserRegisterResponse register(UserRegisterRequest request);

    ValidateTokenResponse validateToken(ValidateTokenRequest request);

}
