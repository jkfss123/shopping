package com.lingshi.shopping_order_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingshi.shopping_common.entity.Orders;

import java.util.List;

public interface OrdersMapper extends BaseMapper<Orders> {
    Orders findById(String id);

    List<Orders> findUserOrdersByStatus(Long userId, Integer status);
}
