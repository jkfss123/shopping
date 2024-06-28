package com.lingshi.common.exception;

/**
 * 自定义业务异常
 * Bus--Business
 */
public class BusException extends RuntimeException{
    private String message;//出现异常消息

    private Integer code;//消息错误

    public BusException(BusCodeEnum busCodeEnum) {
        this.message = busCodeEnum.getMessage();
        this.code = busCodeEnum.getCode();
    }
}
