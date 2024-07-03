package com.lingshi.shopping_manager_api.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.lingshi.common.entity.Admin;
import com.lingshi.common.result.BaseResult;
import com.lingshi.common.service.IAdminService;
import com.lingshi.shopping_manager_api.service.ILoginService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.lingshi.common.exception.BusCodeEnum.LOGIN_ERROR;

@Service
public class LoginServiceImpl implements ILoginService {


    @DubboReference
    private IAdminService adminService;

    @Override
    public BaseResult login(String username, String password) {

        Admin admin = adminService.findByAdminName(username);
        //判断admin对象不为空
        if(Objects.nonNull(admin)){
            //比对密码，密码加密
            String md5Password = SaSecureUtil.md5(password);

            //比较用户输入的密码和数据密码
            if (md5Password.equals(admin.getPassword())){
                //sa-token登录
                StpUtil.login(username);
                //获取token，响应给前端
                String tokenValue = StpUtil.getTokenValue();

                return BaseResult.success(tokenValue);
            }

        }

        //抛出全局异常
//        BusException.busException(LOGIN_ERROR);
        return BaseResult.error(LOGIN_ERROR.getMessage());
    }
}
