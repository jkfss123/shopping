package com.lingshi.common.exception;

import com.sun.net.httpserver.Authenticator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 项目全局错误消息和状态码
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum BusCodeEnum {

    //正常
    SUCCESS(200,"success"),
    //系统异常
    SYSTEM_ERROR(500,"系统异常"),
    //参数异常
    PARAMETER_ERROR(1001,"请求参数异常"),
    DELETE_PERMISSION_ERROR(1002,"当前权限还有角色在使用，不能删除"),
    LOGIN_ERROR(1003,"账号密码异常，请重新输入"),
    NOT_LOGIN_ERROR(1004,"未登陆");
    private Integer code;
    private String message;


}
