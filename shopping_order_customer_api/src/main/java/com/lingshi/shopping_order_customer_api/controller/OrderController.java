package com.lingshi.shopping_order_customer_api.controller;

import com.lingshi.shopping_common.entity.Orders;
import com.lingshi.shopping_common.service.result.BaseResult;
import com.lingshi.shopping_common.service.IOrderService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/orders")
public class OrderController {

    @DubboReference
    private IOrderService orderService;

    /**
     * 新增订单
     * @param orders 订单对象
     * @param userId 用户id
     * @return
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody Orders orders, @RequestHeader Long userId){
        //设置订单用户id
        orders.setUserId(userId);
        Orders orders1 = orderService.add(orders);
        return BaseResult.success(orders1);
    }

    /**
     * 根据订单id查询订单明细
     * @param id 订单id
     * @return
     */
    @GetMapping("/findById")
    public BaseResult findById(String id){
        Orders orders = orderService.findById(id);
        return BaseResult.success(orders);
    }

    /**
     * 查询用户所有订单
     * @param userId 用户id
     * @param status 订单状态
     * @return
     */
    @GetMapping("/findUserOrders")
    public BaseResult findUserOrders(@RequestHeader Long userId,Integer status){
        List<Orders> orders = orderService.findUserOrders(userId,status);
        return BaseResult.success(orders);
    }
}