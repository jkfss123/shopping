package com.lingshi.shopping_common.service;

import com.lingshi.shopping_common.entity.CartGoods;
import com.lingshi.shopping_common.entity.Goods;

import java.util.List;

    public interface ICartService {

        /**
         * 登录用户添加商品到购物车
         * @param userId 用户id
         * @param cartGoods 购物车商品
         */
        void add(Long userId, CartGoods cartGoods);

        /**
         * 登录用户修改购物车商品数量
         * @param userId 用户id
         * @param num 购物车商品
         */
        void update(Long userId,Long goodsId,Integer num);

        /**
         * 登录用户删除购物车商品
         * @param userId 用户id
         * @param goodsId 商品id
         */
        void deleteCartOption(Long userId,Long goodsId);

        /**
         * 查询登录用户的所有购物车商品集合
         * @param userId 用户id
         * @return 商品集合
         */
        List<CartGoods> findByUser(Long userId);


        /**
         * 管理员修改商品同步购物车
         */
        void refreshCartGoods(Goods goods);

        /**
         * 管理员下架商品同步用户的购物车商品
         * @param goodsId 商品id
         */
        void deleteCartOption(Long goodsId);
    }
