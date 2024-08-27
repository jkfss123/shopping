package com.lingshi.shopping_cart_service.service;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.lingshi.shopping_common.constant.RedisKey;
import com.lingshi.shopping_common.entity.CartGoods;
import com.lingshi.shopping_common.entity.Goods;
import com.lingshi.shopping_common.service.ICartService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@DubboService
public class CartServiceImpl implements ICartService {

    @Autowired
    private RedisTemplate<String, CartGoods> redisTemplate;

    @Override
    public void add(Long userId, CartGoods cartGoods) {
        //获取HashKey中指定有的购物车数量
        List<CartGoods> cartList = findByUser(userId);
        for (CartGoods cartGoods1 : cartList) {
            if (cartGoods1.getGoodId().equals(cartGoods.getGoodId())) {
                cartGoods1.setNum(cartGoods.getNum() + cartGoods1.getNum());
                redisTemplate.boundHashOps(RedisKey.CART_LIST).put(userId.toString(), cartList);
                return;
            }
        }
        //直接添加新商品
        cartList.add(cartGoods);
        redisTemplate.boundHashOps(RedisKey.CART_LIST).put(userId.toString(), cartList);
    }

    @Override
    public void update(Long userId, Long goodsId, Integer num) {
        List<CartGoods> cartGoodsList = findByUser(userId);
        for (CartGoods cartGoods : cartGoodsList) {
            if (goodsId.equals(cartGoods.getGoodId())) {
                cartGoods.setNum(num);
                break;
            }
        }
        redisTemplate.boundHashOps(RedisKey.CART_LIST).put(userId.toString(), cartGoodsList);
    }

    @Override
    public void deleteCartOption(Long userId, Long goodsId) {
        List<CartGoods> cartGoodsList = findByUser(userId);

        // 使用stream流式语法
        cartGoodsList = cartGoodsList.stream().filter(cartGoods -> !goodsId.equals(cartGoods.getGoodId())).toList();
        if (CollectionUtils.isNotEmpty(cartGoodsList)) {
            redisTemplate.boundHashOps(RedisKey.CART_LIST).put(userId.toString(), cartGoodsList);
        } else {
            redisTemplate.boundHashOps(RedisKey.CART_LIST).delete(userId.toString());
        }
    }

    @Override
    public List<CartGoods> findByUser(Long userId) {
        // 获取HashKey中指定的有的购物车数据
        Object cartList = redisTemplate.boundHashOps(RedisKey.CART_LIST).get(userId.toString());
        if (Objects.nonNull(cartList)) {
            return (List<CartGoods>) cartList;
        }
        return new ArrayList<>();
    }

    @Override
    public void refreshCartGoods(Goods goods) {
        // 获取购物车所有数据
        Map<Object, Object> entries = redisTemplate.boundHashOps(RedisKey.CART_LIST).entries();
        // map转换为EntrySet
        Set<Map.Entry<Object, Object>> entries1 = entries.entrySet();
        // 循环每个用户的购物车商品
        for (Map.Entry<Object, Object> entry : entries1) {
            // 获取商品集合
            List<CartGoods> cartGoodsList = (List<CartGoods>) entry.getValue();
            // 循环购物车商品
            for (CartGoods cartGoods : cartGoodsList) {
                // 如果修改商品和购物车的相同，则修改
                if (goods.getId().equals(cartGoods.getGoodId())) {
                    cartGoods.setGoodsName(goods.getGoodsName());
                    cartGoods.setHeaderPic(goods.getHeaderPic());
                    cartGoods.setPrice(goods.getPrice());
                }
            }
        }
        // 重新设置购物车
        redisTemplate.boundHashOps(RedisKey.CART_LIST).putAll(entries);
    }

    @Override
    public void deleteCartOption(Long goodsId) {
        // 获取购物车所有数据
        Map<Object, Object> entries = redisTemplate.boundHashOps(RedisKey.CART_LIST).entries();
        // map转换为EntrySet
        Set<Map.Entry<Object, Object>> entries1 = entries.entrySet();
        // 循环每个用户的购物车商品
        for (Map.Entry<Object, Object> entry : entries1) {
            // 获取商品集合
            List<CartGoods> cartGoodsList = (List<CartGoods>) entry.getValue();
            // 循环购物车商品
            for (CartGoods cartGoods : cartGoodsList) {
                // 如果要删除的商品和购物车id相同，则删除
                if (goodsId.equals(cartGoods.getGoodId())) {
                    cartGoodsList.remove(cartGoods);
                    break;
                }
            }
        }
        // 重新设置购物车
        redisTemplate.boundHashOps(RedisKey.CART_LIST).putAll(entries);
    }
}
