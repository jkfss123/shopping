package com.lingshi.shopping_cart_customer_api.controller;

import com.lingshi.shopping_common.entity.CartGoods;
import com.lingshi.shopping_common.service.result.BaseResult;
import com.lingshi.shopping_common.service.ICartService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/cart")
public class CartController {

    @DubboReference
    private ICartService cartService;

    /**
     * 添加购物车
     * @param cartGoods 购车车商品
     * @param userId 用户id
     * @return
     */
    @PostMapping("/addCart")
    public BaseResult addCart(@RequestBody CartGoods cartGoods, @RequestHeader Long userId){
        cartService.add(userId,cartGoods);
        return BaseResult.success();
    }

    /**
     * 修改购物车商品数量
     * @param goodId 商品id
     * @param num 商品数量
     * @param userId 用户id
     * @return
     */
    @PostMapping("/handleCart")
    public BaseResult handleCart(Long goodId,Integer num, @RequestHeader Long userId){
        cartService.update(userId,goodId,num);
        return BaseResult.success();
    }

    /**
     * 删除购物车指定商品
     * @param goodId 商品id
     * @param userId 用户id
     * @return
     */
    @DeleteMapping("/deleteCart")
    public BaseResult deleteCart(Long goodId, @RequestHeader Long userId){
        cartService.deleteCartOption(userId,goodId);
        return BaseResult.success();
    }

    /**
     * 查询所有购物车商品
     * @param userId 用户id
     * @return
     */
    @GetMapping("/findCartList")
    public BaseResult findCartList(@RequestHeader Long userId){
        List<CartGoods> cartGoodsList = cartService.findByUser(userId);
        return BaseResult.success(cartGoodsList);
    }
}