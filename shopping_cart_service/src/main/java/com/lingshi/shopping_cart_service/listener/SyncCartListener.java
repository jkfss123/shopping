//package com.lingshi.shopping_cart_service.listener;
//
//import com.lingshi.shopping_common.constant.Const;
//import com.lingshi.shopping_common.entity.Goods;
//import com.lingshi.shopping_common.service.ICartService;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//@RocketMQMessageListener(consumerGroup = Const.SYNC_CART_GROUP,topic = Const.SYNC_CART_QUEUE)
//public class SyncCartListener implements RocketMQListener<Goods> {
//    @Autowired
//    private ICartService cartService;
//
//
//    @Override
//    public void onMessage(Goods goods) {
//        cartService.refreshCartGoods(goods);
//    }
//}
