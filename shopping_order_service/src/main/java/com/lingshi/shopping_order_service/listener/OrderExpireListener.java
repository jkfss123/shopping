package com.lingshi.shopping_order_service.listener;

import com.lingshi.shopping_common.constant.Const;
import com.lingshi.shopping_common.entity.Orders;
import com.lingshi.shopping_common.exception.OrderStatusEnums;
import com.lingshi.shopping_common.service.IOrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(consumerGroup = Const.SYNC_ORDER_GROUP, topic = Const.SYNC_ORDER_QUEUE)
public class OrderExpireListener implements RocketMQListener<String> {

    @Autowired
    private IOrderService orderService;

    @Override
    public void onMessage(String orderId) {

        //查询订单
        Orders orders = orderService.findById(orderId);
        //订单如果未支付
        if (OrderStatusEnums.NO_PAY.getType().equals(orders.getStatus())) {
            //修改订单状态为关闭
            orders.setStatus(OrderStatusEnums.ORDER_CLOSE.getType());
            //修改订单
            orderService.update(orders);
        }

    }
}