package com.lingshi.shopping_order_customer_api.controller;

import com.alibaba.fastjson2.JSON;
import com.lingshi.shopping_common.entity.Orders;
import com.lingshi.shopping_common.entity.Payment;
import com.lingshi.shopping_common.exception.OrderStatusEnums;
import com.lingshi.shopping_common.service.result.BaseResult;
import com.lingshi.shopping_common.service.IOrderService;
import com.lingshi.shopping_common.service.IZfbPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 订单支付
 */
@RestController
@RequestMapping("/user/payment")
public class PaymentController {

    @DubboReference
    private IOrderService orderService;

    @DubboReference
    private IZfbPayService zfbPayService;

    /**
     * 预请求返回支付二维码
     * @param orderId
     * @return
     */
    @PostMapping("/pcPay")
    public BaseResult pcPay(String orderId){
        //1，根据订单获取订单信息
        Orders orders = orderService.findById(orderId);
        //2. 支付宝预请求获取二维码
        String qrUrl = zfbPayService.pcPay(orders);
        return BaseResult.success(qrUrl);
    }

    /**
     * 支付宝支付回调请求
     * @param request 请求对象，携带支付结果参数
     */
    @PostMapping("/success/notify")
    public void notify(HttpServletRequest request){

        //验签
        if(zfbPayService.checkSign(request)){
            //获取订单id
            String outTradeNo = request.getParameter("out_trade_no");
            //支付状态
            String tradeStatus = request.getParameter("trade_status");

            if("TRADE_SUCCESS".equals(tradeStatus)){
                Orders orders = orderService.findById(outTradeNo);
                //修改订单状态
                orders.setStatus(OrderStatusEnums.OK_PAY.getType());
                orderService.update(orders);
                //创建交易记录对象
                Payment payment = new Payment();
                payment.setContent(JSON.toJSONString(request.getParameterMap()));
                payment.setCreateTime(LocalDateTime.now());
                payment.setTradeState(tradeStatus);
                payment.setTradeType("支付宝支付");
                payment.setTransactionId(request.getParameter("trade_no"));
                //添加交易记录
                zfbPayService.addPayment(payment);
            }
        }

    }
}
