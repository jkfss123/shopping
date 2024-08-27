//package com.lingshi.shopping_search_service.listener;
//
//import com.lingshi.shopping_common.constant.Const;
//import com.lingshi.shopping_common.service.IGoodsSearchService;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//// 监听同步商品消息
////@Service
////@RocketMQMessageListener(consumerGroup = Const.DEL_GOODS_GROUP,topic = Const.DEL_GOODS_QUEUE)
////public class SyncGoodsListener implements RocketMQListener<GoodsDesc> {
////    @Autowired
////    private IGoodsSearchService goodsSearchService;
////    @Override
////    public void onMessage(GoodsDesc goodsDesc) {
////        System.out.println("同步es商品");
////        goodsSearchService.syncGoodsToES(goodsDesc);
////    }
////}
//
//
//// 监听删除商品消息
//@Component
//@RocketMQMessageListener(consumerGroup = Const.DEL_GOODS_GROUP,topic = Const.DEL_GOODS_QUEUE)
//public class SyncGoodsListener implements RocketMQListener<Long> {
//    @Autowired
//    private IGoodsSearchService goodsSearchService;
//    @Override
//    public void onMessage(Long goodsId) {
//        System.out.println("删除es商品");
//        goodsSearchService.delete(goodsId);
//    }
//}


