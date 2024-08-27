package com.lingshi.shopping_goods_service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.entity.SeckillGoods;
import com.lingshi.shopping_common.service.ISecKillGoodsService;
import com.lingshi.shopping_goods_service.mapper.SecKillGoodsMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class SecKillGoodsServiceImpl implements ISecKillGoodsService {
    @Autowired
    private SecKillGoodsMapper seckillGoodsMapper;


    @Override
    public void add(SeckillGoods seckillGoods) {
        seckillGoodsMapper.insert(seckillGoods);
    }


    @Override
    public void update(SeckillGoods seckillGoods) {
        seckillGoodsMapper.updateById(seckillGoods);
    }


    @Override
    public Page<SeckillGoods> findPage(int page, int size) {
        return seckillGoodsMapper.selectPage(new Page(page, size), null);
    }
}

