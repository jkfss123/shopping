package com.lingshi.shopping_common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatusEnums {
    //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
    NO_PAY("1","未付款"),
    OK_PAY("2","已付款"),
    ORDER_CLOSE("3","交易关闭");
    private String type;
    private String message;
}