package com.xcxcxcxcx.myshop.user.service.impl;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.xcxcxcxcx.myshop.IUserCoreSerivce;
import com.xcxcxcxcx.myshop.contants.ResponseCodeEnum;
import com.xcxcxcxcx.myshop.dto.*;
import com.xcxcxcxcx.myshop.user.dal.entity.User;
import com.xcxcxcxcx.myshop.user.dal.persistence.UserMapper;
import com.xcxcxcxcx.myshop.user.exception.ServiceException;
import com.xcxcxcxcx.myshop.user.exception.ValidateException;
import com.xcxcxcxcx.myshop.user.util.ExceptionUtils;
import com.xcxcxcxcx.myshop.user.util.JwtTokenUtils;
import com.xcxcxcxcx.service.support.core.request.AbstractRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XCXCXCXCX
 * @date 2018/10/30
 * @comments
 */
public class UserCoreServiceImpl implements IUserCoreSerivce {

    private static Logger logger = LoggerFactory.getLogger(UserCoreServiceImpl.class);

    @Autowired
    UserMapper userMapper;

    public UserLoginResponse login(UserLoginRequest request) {
        UserLoginResponse response = new UserLoginResponse();

        try{
            validateRequest(request);

            User user = userMapper.getUserByUsername(request.getUsername());

            if (request.getPassword().equals(user.getPassword())) {
                response.setId(user.getId());
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("uid",user.getId());
                map.put("exp", DateTime.now().plusSeconds(40).toDate().getTime()/1000);
                response.setToken(JwtTokenUtils.generatorToken(map));
                response.setCode(ResponseCodeEnum.SUCCESS.getCode());
                response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());
                return response;
            } else {
                response.setCode(ResponseCodeEnum.USER_OR_PASSWORD_NOT_EXIST.getCode());
                response.setMsg(ResponseCodeEnum.USER_OR_PASSWORD_NOT_EXIST.getMsg());
                return response;
            }

        }catch (Exception e){
            logger.error("login occur exception :"+e);
            ServiceException serviceException=(ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        }finally {
            logger.info("登陆response:{" + response.toString() + "}");
        }

        return response;
    }

    public UserRegisterResponse register(UserRegisterRequest request) {

        UserRegisterResponse response = new UserRegisterResponse();

        try {

            validateRequest(request);

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setAlipayNumber(request.getAlipayNumber());
            user.setWechatNumber(request.getWechatNumber());
            user.setStatus(1);
            user.setVersionNumber(1);

            int row = userMapper.insertSelective(user);
            if (row < 1) {
                response.setCode(ResponseCodeEnum.SYSTEM_BUSY.getCode());
                response.setMsg(ResponseCodeEnum.SYSTEM_BUSY.getMsg());
                return response;
            }
            response.setCode(ResponseCodeEnum.SUCCESS.getCode());
            response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());
        } catch (Exception e) {
            logger.error("register occur exception :"+e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        } finally {
            logger.info("注册response:{" + response.toString() + "}");
        }

        return response;

    }

    public ValidateTokenResponse validateToken(ValidateTokenRequest request) {

        ValidateTokenResponse response = new ValidateTokenResponse();

        try{
            validateRequest(request);

            Claims claims = JwtTokenUtils.phaseToken(request.getToken());
            response.setId(Long.parseLong(claims.get("id").toString()));
            response.setCode(ResponseCodeEnum.SUCCESS.getCode());
            response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());

        }catch (ExpiredJwtException e){
            logger.error("token expire :"+e);
            response.setCode(ResponseCodeEnum.INVALID_TOKEN.getCode());
            response.setMsg(ResponseCodeEnum.INVALID_TOKEN.getMsg());
        }catch (SignatureException e1){
            logger.error("signature validate failed :"+e1);
            response.setCode(ResponseCodeEnum.SIGNATURE_VALIDATE_FAILED.getCode());
            response.setMsg(ResponseCodeEnum.SIGNATURE_VALIDATE_FAILED.getMsg());
        }catch (Exception e){
            logger.error("validateToken occur exception :" + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        }finally {
            logger.info("validateToken response: {"+ response +"}");
        }

        return response;
    }

    private void validateRequest(AbstractRequest request) {
        if(request == null){
            throw new ValidateException("请求对象为空");
        }

        if (request instanceof UserLoginRequest) {
            UserLoginResponse response = new UserLoginResponse();
            if (StringUtils.isEmpty(((UserLoginRequest) request).getUsername())) {
                throw new ValidateException("用户名为空");
            }
            if (StringUtils.isEmpty(((UserLoginRequest) request).getPassword())) {
                throw new ValidateException("密码为空");
            }
        }

        if (request instanceof UserRegisterRequest) {
            if (StringUtils.isEmpty(((UserRegisterRequest) request).getUsername())) {
                throw new ValidateException("用户名为空");
            }
            if (StringUtils.isEmpty(((UserRegisterRequest) request).getPassword())) {
                throw new ValidateException("密码为空");
            }
            if (StringUtils.isEmpty(((UserRegisterRequest) request).getAlipayNumber())
                    && StringUtils.isEmpty(((UserRegisterRequest) request).getWechatNumber())) {
                throw new ValidateException("支付账户为空");
            }
            if(null != userMapper.getUserByUsername(((UserRegisterRequest) request).getUsername())){
                throw new ValidateException("用户名已存在");
            }

        }

        if(request instanceof ValidateTokenRequest){
            if(StringUtils.isEmpty(((ValidateTokenRequest) request).getToken())){
                throw new ValidateException("token为空");
            }
        }

    }

}
