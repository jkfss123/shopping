package com.lingshi.shopping_common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.entity.Orders;
import com.lingshi.shopping_common.entity.SeckillGoods;

public interface ISeckillService {
    /**
     * 前台用户查询秒杀商品
     * @param page 页数
     * @param size 每页条数
     * @return 查询结果
     */
    Page<SeckillGoods> findPageByRedis(int page, int size);


    /**
     * 查询秒杀商品详情
     * @param goodsId 秒杀商品对应的商品Id
     * @return 查询结果
     */
    SeckillGoods findSeckillGoodsByRedis(Long goodsId);


    /**
     * 查询秒杀商品详情
     * @param goodsId 秒杀商品对应的商品Id
     * @return 查询结果
     */
    SeckillGoods findSeckillGoodsByMySQL(Long goodsId);


    /**
     * 添加秒杀商品到Redis中 （抽取公共方法）
     * @param seckillGoods
     */
    void addSeckillGoodsToRedis(SeckillGoods seckillGoods);


    /**
     * 生成秒杀订单
     * @param orders 订单数据
     * @return
     */
    Orders createOrder(Orders orders);


    /**
     * 根据id查询秒杀订单
     * @param id 订单id
     * @return
     */
    Orders findOrder(String id);


    /**
     * 支付秒杀订单
     * @param orderId 订单id
     * @return
     */
    Orders pay(String orderId);
}