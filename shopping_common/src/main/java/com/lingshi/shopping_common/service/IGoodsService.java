package com.lingshi.shopping_common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.entity.Goods;
import com.lingshi.shopping_common.entity.GoodsDesc;

import java.util.List;

public interface IGoodsService {
    /**
     * 商品服务
     */
        // 新增商品
        void add(Goods goods);
        // 修改商品
        void update(Goods goods);
        // 根据id查询商品详情
        Goods findById(Long id);
        // 上架/下架商品
        void putAway(Long id,Boolean isMarketable);
        // 分页查询
        Page<Goods> search(Goods goods, int page, int size);
        List<GoodsDesc> findAll();

        GoodsDesc findGoodsDescById(Long id);
    }

