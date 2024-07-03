package com.lingshi.common.security;

import cn.dev33.satoken.stp.StpUtil;
import com.lingshi.common.exception.BusCodeEnum;
import com.lingshi.common.exception.BusException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static com.lingshi.common.exception.BusCodeEnum.NOT_LOGIN_ERROR;

public class CheckLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断是否登录

        if (!StpUtil.isLogin()) {
            BusException.busException(NOT_LOGIN_ERROR);

        }
        return true;//放行
    }

}
