package com.lingshi.common.exception;


import cn.dev33.satoken.exception.NotPermissionException;
import com.lingshi.common.result.BaseResult;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 捕获业务异常并处理
     * @param e
     * @return
     */
    @ExceptionHandler(BusException.class)
    public BaseResult busExceptionHandler(BusException e){
        System.out.println("e = "+ e);
        return BaseResult.error(e.getCode(), e.getMessage());
    }

    /**
     * 捕获业务异常并处理
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResult baseResult(HttpMessageNotReadableException e){
        System.out.println("e = " + e);
        return BaseResult.error(BusCodeEnum.PARAMETER_ERROR.getCode(),BusCodeEnum.PARAMETER_ERROR.getMessage());

    }

    /**
     * 捕获权限异常
     * @param e
     * @return
     */
    @ExceptionHandler(NotPermissionException.class)
    public BaseResult baseResult(NotPermissionException e){
        System.out.println("e = " + e);
        return BaseResult.error(e.getCode(),e.getMessage());

    }
}
