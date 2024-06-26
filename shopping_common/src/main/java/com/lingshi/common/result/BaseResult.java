package com.lingshi.common.result;

import com.alibaba.fastjson2.annotation.JSONType;
import com.lingshi.common.entity.Brand;

import java.io.Serializable;

public class BaseResult implements Serializable {
    private Integer code;

    private String message;

    private Object data;

    public BaseResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResult() {

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static BaseResult success() {
        BaseResult result = new BaseResult();
        result.code = 200;
        result.message = "success";
        return result;
    }

    public static BaseResult success(Object data, String message) {
        BaseResult result = new BaseResult();
        result.code = 200;
        result.message = message;
        return result;
    }

    public static BaseResult success(Object data) {
        BaseResult result = new BaseResult();
        result.code = 200;
        result.data = data;
        return result;
    }

    public static BaseResult error() {
        BaseResult result = new BaseResult();
        result.code = 500;
        result.message = "error";
        return result;
    }

    public static BaseResult error(String message) {
        BaseResult result = new BaseResult();
        result.code = 500;
        result.message = message;
        return result;
    }
    public static BaseResult error(Integer code,String message) {
        BaseResult result = new BaseResult();
        result.code = code;
        result.message = message;
        return result;
    }
    public static BaseResult error(int code, String message) {
        BaseResult result = new BaseResult();
        result.code = code;
        result.message = message;
        return result;
    }
}
