package com.lingshi.shopping_order_service.service;

import cn.hutool.core.util.IdUtil;
import com.lingshi.shopping_common.constant.Const;
import com.lingshi.shopping_common.entity.CartGoods;
import com.lingshi.shopping_common.entity.Orders;
import com.lingshi.shopping_common.exception.OrderStatusEnums;
import com.lingshi.shopping_common.service.ICartService;
import com.lingshi.shopping_common.service.IOrderService;
import com.lingshi.shopping_order_service.mapper.CartGoodsMapper;
import com.lingshi.shopping_order_service.mapper.OrdersMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@DubboService
@Transactional
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private CartGoodsMapper cartGoodsMapper;

    @DubboReference
    private ICartService cartService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public Orders add(Orders orders) {
        //设置订单id
        long orderId = IdUtil.getSnowflakeNextId();
        orders.setId(String.valueOf(orderId));
        //设置下单时间
        orders.setCreateTime(LocalDateTime.now());
        //普通订单默认没有状态，秒杀订单传进来对象，就是支付成给的对象，所有有状态
        if(orders.getStatus()==null){
            //设置订单状态
            orders.setStatus(OrderStatusEnums.NO_PAY.getType());
        }

        //计算订单总金额
        BigDecimal payment = BigDecimal.ZERO;

        List<CartGoods> list = orders.getCartGoods();
        for (CartGoods goods : list) {
            //计算单个商品的总价 ：商品数量 * 单价
            BigDecimal price = goods.getPrice().multiply(BigDecimal.valueOf(goods.getNum()));
            //计算总金额
            payment = payment.add(price);
        }

        //设置订单总金额
        orders.setPayment(payment);

        //保存订单
        ordersMapper.insert(orders);
        //保存订单明细（商品）
        List<CartGoods> cartGoods = orders.getCartGoods();
        for (CartGoods goods : cartGoods) {
            //设置订单id
            goods.setOrderId(orders.getId());
            //保存订单
            cartGoodsMapper.insert(goods);

            //删除购物车
            cartService.deleteCartOption(orders.getUserId(),goods.getGoodId());
        }

        //将订单编号发送到MQ
        //发送延时消息，30m后判断订单是否支付
        //延时等级1到16分别表示 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h

        rocketMQTemplate.syncSend(Const.SYNC_ORDER_QUEUE, MessageBuilder.withPayload(orders.getUserId()).build(),15000,4);

        return orders;
    }

    @Override
    public void update(Orders orders) {
        ordersMapper.updateById(orders);
    }

    @Override
    public Orders findById(String id) {
        Orders orders =  ordersMapper.findById(id);
        return orders;
    }

    @Override
    public List<Orders> findUserOrders(Long userId, Integer status) {
        List<Orders>  ordersList = ordersMapper.findUserOrdersByStatus(userId,status);
        return ordersList;
    }
}