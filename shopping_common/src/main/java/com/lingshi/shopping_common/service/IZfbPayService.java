package com.lingshi.shopping_common.service;

import com.lingshi.shopping_common.entity.Orders;
import com.lingshi.shopping_common.entity.Payment;
import jakarta.servlet.http.HttpServletRequest;

public interface IZfbPayService {
    /**
     * 支付返回二维码（地址）
     * @param orders 订单
     * @return
     */
    String pcPay(Orders orders);

    /**
     * 验签
     * @param request 请求对象（包含支付宝回调的参数）
     * @return
     */
    boolean checkSign(HttpServletRequest request);

    /**
     * 添加交易记录
     * @param payment
     */
    void addPayment(Payment payment);
}