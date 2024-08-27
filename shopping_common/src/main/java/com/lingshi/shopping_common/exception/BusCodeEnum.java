package com.lingshi.shopping_common.exception;

import lombok.AllArgsConstructor;
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

    NOT_LOGIN_ERROR(401,"未登陆"),
    //系统异常
    SYSTEM_ERROR(500,"系统异常"),
    //参数异常
    PARAMETER_ERROR(1001,"请求参数异常"),
    DELETE_PERMISSION_ERROR(1002,"当前权限还有角色在使用，不能删除"),
    LOGIN_ERROR(1003,"账号密码异常，请重新输入"),

    INSERT_OR_PRODUCT_TYPE_ERROR(1004,"商品分类最多三级"),

    DELETE_PRODUCT_TYPE_ERROR(1005,"此商品分类下面还有子分类，不能删除"),

    SEND_MESSAGE_ERROR(1007,"发送短信异常"),

    VERIFY_CODE_ERROR(1008,"手机验证码错误"),

    REGISTER_PHONE_EXISTS(1009, "手机号已经注册，请登录"),
    LOGIN_NAME_PASSWORD_ERROR(1009, "账号密码错误"),
    REGISTER_USERNAME_EXISTS(1010, "账号已经存在，请换一个账号"),
    REGISTER_NOT_EXISTS(1011, "手机号未注册，请先注册"),
    ZFG_PAY_ERROR(1012, "支付宝支付失败"),
    CHECK_ZFB_SIGNATURE_ERROR(1013, "支付宝验签异常"),
    SECOND_KILL_GOODS_STOCK_COUNT_ERROR(1014, "商品库存不足"),
    SECOND_KILL_ORDER_EXIPRE(1015, "秒杀订单已经过期");


    private Integer code;
    private String message;


}
