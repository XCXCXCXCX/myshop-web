package com.xcxcxcxcx.myshop.usercenter.controller;

import com.xcxcxcxcx.common.annotation.Anoymous;
import com.xcxcxcxcx.myshop.IUserCoreSerivce;
import com.xcxcxcxcx.myshop.dto.UserLoginRequest;
import com.xcxcxcxcx.myshop.dto.UserLoginResponse;
import com.xcxcxcxcx.myshop.dto.UserRegisterRequest;
import com.xcxcxcxcx.myshop.dto.UserRegisterResponse;
import com.xcxcxcxcx.myshop.usercenter.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author XCXCXCXCX
 * @date 2018/11/8
 * @comments
 */
@RestController("/user")
public class UserCoreController extends BaseController{

    @Autowired
    IUserCoreSerivce userCoreSerivce;

    @Anoymous
    @PostMapping("/login")
    public ResponseData login(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              HttpServletResponse httpServletResponse){

        UserLoginRequest request = new UserLoginRequest();
        request.setUsername(username);
        request.setPassword(password);
        UserLoginResponse response = userCoreSerivce.login(request);

        if("00000000".equals(response.getCode())){
            httpServletResponse.addHeader("Set-Cookie",
                    "access_token="+response.getToken()+";Path=/;HttpOnly");

            ResponseData responseData = ResponseData.buildSuccess(response.getId());

            return responseData;
        }

        return ResponseData.buildFailed(response.getCode(), response.getMsg(), null);

    }

    @Anoymous
    @PostMapping("/register")
    public ResponseData register(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 @RequestParam(value = "alipayNumber",required = false) String alipayNumber,
                                 @RequestParam(value = "wechatNumber",required = false)String wechatNumber){


        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setPassword(password);
        registerRequest.setAlipayNumber(alipayNumber);
        registerRequest.setWechatNumber(wechatNumber);
        UserRegisterResponse registerResponse = userCoreSerivce.register(registerRequest);
        if("00000000".equals(registerResponse.getCode())){
            return ResponseData.buildSuccess(null);
        }

        return ResponseData.buildFailed(registerResponse.getCode(), registerResponse.getMsg(), null);
    }

}
