package com.lingshi.shopping_seckill_customer_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.entity.Orders;
import com.lingshi.shopping_common.entity.SeckillGoods;
import com.lingshi.shopping_common.service.result.BaseResult;
import com.lingshi.shopping_common.service.ISeckillService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/user/seckillGoods")
public class SeckillController {

    @DubboReference
    private ISeckillService seckillService;


//    @DubboReference
//    private IOrderService orderService;

    /**
     * 分页查询秒杀商品
     * @param page 页码
     * @param size 每页条数
     * @return 秒杀商品集合
     */
    @GetMapping("/findPage")
    public BaseResult findPage(int page, int size){
        Page<SeckillGoods> seckillGoodsPage = seckillService.findPageByRedis(page, size);
        return BaseResult.success(seckillGoodsPage);
    }

    /**
     * 查询秒杀商品详情
     * @param id 商品id
     * @return 秒杀商品详情
     */
    @GetMapping("/findById")
    public BaseResult findById(Long id){
        //从redis查询
        SeckillGoods seckillGoods = seckillService.findSeckillGoodsByRedis(id);
        if(Objects.isNull(seckillGoods)){
            //如果redis没有查询到，从MySQL查询
            seckillGoods = seckillService.findSeckillGoodsByMySQL(id);
        }

        return BaseResult.success(seckillGoods);
    }

    /**
     * 秒杀商品下单
     * @param orders 订单
     * @param userId 用户id
     * @return
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody Orders orders, @RequestHeader Long userId){
        orders.setUserId(userId);
        return BaseResult.success(seckillService.createOrder(orders));
    }

    /**
     * 根据订单id查询秒杀订单
     * @param id 秒杀订单id
     * @return
     */
    @GetMapping("/findOrder")
    public BaseResult add(String id){
        return BaseResult.success(seckillService.findOrder(id));
    }


//    @GetMapping("/pay")
//    public BaseResult pay(String id){
//        //对支付成功的 秒杀订单处理
//        Orders orders = seckillService.pay(id);
//
//        //将订单数据同步mysql
//        orderService.add(orders);
//
//        return BaseResult.success(id);
//    }


}