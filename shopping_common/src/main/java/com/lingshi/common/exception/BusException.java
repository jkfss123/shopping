package com.lingshi.common.exception;

import lombok.*;

/**
 * 自定义业务异常
 * Bus--Business
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusException extends RuntimeException{
    private String msg;//出现异常消息

    private Integer code;//消息错误

    public BusException(BusCodeEnum busCodeEnum) {
        this.msg = busCodeEnum.getMessage();
        this.code = busCodeEnum.getCode();
    }

    public static void busException(BusCodeEnum busCodeEnum)
    {
        throw new BusException(busCodeEnum);

    }
    @Override
    public String getMessage(){
        return msg;
    }
    public Integer getCode(){
        return code;
    }
}
