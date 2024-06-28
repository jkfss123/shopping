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
    PARAMETER_ERROR(501,"请求参数异常");
    private Integer code;
    private String message;


}
