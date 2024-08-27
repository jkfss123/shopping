package com.lingshi.shopping_seckill_service.redis;

import cn.hutool.core.util.StrUtil;
import com.lingshi.shopping_common.constant.RedisKey;
import com.lingshi.shopping_common.entity.Orders;
import com.lingshi.shopping_common.entity.SeckillGoods;
import com.lingshi.shopping_common.service.ISeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpireListener extends KeyExpirationEventMessageListener {


    @Autowired
    private ISeckillService iSeckillService;

    @Autowired
    private RedisTemplate redisTemplate;

    public RedisKeyExpireListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * rediskey过期触发
     *
     * @param message message must not be {@literal null}.
     * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        //过期的订单id
        String orderId = new String(message.getBody());
        //获取副本订单的数据
        Orders order = iSeckillService.findOrder(StrUtil.format(RedisKey.SECOND_KILL_ORDER_COPY, orderId));

        //获取商品id
        Long goodId = order.getCartGoods().get(0).getGoodId();
        //获取购买数量
        Integer num = order.getCartGoods().get(0).getNum();


        SeckillGoods seckillGoods = iSeckillService.findSeckillGoodsByRedis(goodId);
        seckillGoods.setStockCount(seckillGoods.getStockCount() + num);

        //将库存退回给redis商品
        redisTemplate.boundHashOps(RedisKey.SECOND_KILL_GOODS).put(goodId, seckillGoods);
        //删除副本订单
        redisTemplate.delete(StrUtil.format(RedisKey.SECOND_KILL_ORDER_COPY, orderId));

    }
}