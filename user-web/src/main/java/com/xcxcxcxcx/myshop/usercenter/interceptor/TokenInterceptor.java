package com.xcxcxcxcx.myshop.usercenter.interceptor;

import com.xcxcxcxcx.common.BaseController;
import com.xcxcxcxcx.common.annotation.Anoymous;
import com.xcxcxcxcx.common.constants.MyshopWebConstant;
import com.xcxcxcxcx.common.utils.CookieUtil;
import com.xcxcxcxcx.myshop.IUserCoreSerivce;
import com.xcxcxcxcx.myshop.dto.ValidateTokenRequest;
import com.xcxcxcxcx.myshop.dto.ValidateTokenResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author XCXCXCXCX
 * @date 2018/11/8
 * @comments
 */
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter{

    private final String ACCESS_TOKEN="access_token";

    @Autowired
    IUserCoreSerivce userCoreSerivce;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //不是handlerMethod的，不处理
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Object bean = handlerMethod.getBean();

        //加了Anoymous注解的，不处理
        if(isAnoymous(handlerMethod)){
            return true;
        }

        if(!(bean instanceof BaseController)){
            throw new RuntimeException("must extend basecontroller");
        }
        //从cookie获取token
        String token = CookieUtil.getCookieValue(request, ACCESS_TOKEN);
        boolean isAjax=CookieUtil.isAjax(request);
        if(StringUtils.isEmpty(token)){
            if(isAjax){
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write("{\"code\":\"-1\",\"msg\":\"error\"}");
                return false;
            }
            response.sendRedirect(MyshopWebConstant.MYSHOP_SSO_ACCESS_URL);
            return false;
        }

        ValidateTokenRequest validateTokenRequest = new ValidateTokenRequest();
        validateTokenRequest.setToken(token);
        ValidateTokenResponse validateTokenResponse = userCoreSerivce.validateToken(validateTokenRequest);

        if("00000000".equals(validateTokenResponse.getCode())){
            if(isAjax){
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write("{\"code\":\""+validateTokenResponse.getCode()+"\"" +
                        ",\"msg\":\""+validateTokenResponse.getMsg()+"\"}");
                return false;
            }
            return super.preHandle(request, response, handler);
        }

        response.sendRedirect(MyshopWebConstant.MYSHOP_SSO_ACCESS_URL);
        return false;
    }

    private boolean isAnoymous(HandlerMethod handlerMethod) {

        Anoymous anoymous1 = handlerMethod.getBean().getClass().getAnnotation(Anoymous.class);
        Anoymous anoymous2 = handlerMethod.getMethodAnnotation(Anoymous.class);
        if(anoymous1 == null && anoymous2 == null){
            return false;
        }

        return true;
    }
}
