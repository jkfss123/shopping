package com.lingshi.shopping_goods_service.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.constant.Const;
import com.lingshi.shopping_common.entity.*;
import com.lingshi.shopping_common.service.IGoodsService;
import com.lingshi.shopping_goods_service.mapper.GoodsImageMapper;
import com.lingshi.shopping_goods_service.mapper.GoodsMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Transactional
@DubboService
@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsImageMapper goodsImageMapper;

    @Autowired
    private RocketMQTemplate rocketMQTemplate; // RocketMQ工具类


//    @DubboReference
//    private IGoodsSearchService goodsSearchService;

    // 同步商品数据主题
    private final String SYNC_GOOD_QUEUE = "sync_goods_queue";
    // 删除商品数据主题
    private final String DEL_GOOD_QUEUE = "del_goods_queue";

    @Override
    public void add(Goods goods) {
        //保存商品数据
        goodsMapper.insert(goods);

        //保存商品图片
        List<GoodsImage> goodsImages = goods.getImages();
        for (GoodsImage goodsImage : goodsImages) {
            goodsImageMapper.insert(goodsImage);
        }
        //保存商品规格选项
        //获取商品的规格
        List<Specification> specifications = goods.getSpecifications();
        //创建集合储存规格项目
        List<SpecificationOption> options = new ArrayList<>();
        for (Specification specification : specifications) {
            List<SpecificationOption> specificationOptions = specification.getSpecificationOptions();
            options.addAll(specificationOptions);
        }
        //循环规格，保存数据
        for (SpecificationOption option : options){
            //新增商品的规格项
            goodsMapper.addSpecificationOption(goods.getId(),option.getId());

        }
        //查询出ES对象
        GoodsDesc goodsDesc = this.findGoodsDescById(goods.getId());
        //同步到ES
//        goodsSearchService.syncGoodsToES(goodsDesc);
        rocketMQTemplate.syncSend(Const.SYNC_GOODS_QUEUE,goodsDesc);
    }

    @Override
    public void update(Goods goods) {
        //保存商品数据
        goodsMapper.updateById(goods);

        //删除原有图片
        LambdaUpdateWrapper<GoodsImage> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GoodsImage::getGoodsId,goods.getId());
        goodsImageMapper.delete(updateWrapper);

        //保存商品图片
        List<GoodsImage> goodsImages = goods.getImages();
        for (GoodsImage goodsImage : goodsImages) {
            goodsImageMapper.insert(goodsImage);

            //删除商品现有的规格项
            goodsMapper.deleteSpecificationOptionByGoodsId(goods.getId());

        }
        //保存商品规格选项
        //获取商品的规格
        List<Specification> specifications = goods.getSpecifications();
        //创建集合储存规格项目
        List<SpecificationOption> options = new ArrayList<>();
        for (Specification specification : specifications) {
            List<SpecificationOption> specificationOptions = specification.getSpecificationOptions();
            options.addAll(specificationOptions);
        }
        //循环规格，保存数据
        for (SpecificationOption option : options){
            //新增商品的规格项
            goodsMapper.addSpecificationOption(goods.getId(),option.getId());
        }
        //查询出ES对象
        GoodsDesc goodsDesc = this.findGoodsDescById(goods.getId());
        //同步到ES
//        goodsSearchService.syncGoodsToES(goodsDesc);
        rocketMQTemplate.syncSend(Const.SYNC_GOODS_QUEUE,goodsDesc);
        //同步购物车
        rocketMQTemplate.syncSend(Const.DEL_CART_QUEUE,goods);
    }

    @Override
    public Goods findById(Long id) {
        Goods goods = goodsMapper.findById(id);
        return goods;
    }

    @Override
    public void putAway(Long gid, Boolean isMarketable) {
        Goods goods = new Goods();
        goods.setId(gid);
        goods.setIsMarketable(isMarketable);
        goodsMapper.updateById(goods);

        if (isMarketable){
            //查询出ES对象
            GoodsDesc goodsDesc = this.findGoodsDescById(goods.getId());
            //同步到ES
//            goodsSearchService.syncGoodsToES(goodsDesc);
            rocketMQTemplate.syncSend(Const.SYNC_GOODS_QUEUE,goodsDesc);
        }else {
            //下架商品就删除
//            goodsSearchService.delete(gid);
            rocketMQTemplate.syncSend(Const.DEL_GOODS_QUEUE,gid);
            //同步购物车
            rocketMQTemplate.syncSend(Const.DEL_CART_QUEUE,goods);
        }
    }

    @Override
    public Page<Goods> search(Goods goods, int page, int size) {
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(goods) && StrUtil.isNotBlank(goods.getGoodsName())){
            queryWrapper.like(Goods::getGoodsName,goods.getGoodsName());
            queryWrapper.or();
            queryWrapper.like(Goods::getCaption,goods.getGoodsName());
        }
        return goodsMapper.selectPage(new Page<>(page,size),queryWrapper);
    }

    @Override
    public List<GoodsDesc> findAll() {
        List<GoodsDesc> all = goodsMapper.findAll();
        return all;
    }

    @Override
    public GoodsDesc findGoodsDescById(Long id) {
        GoodsDesc goodsDesc = goodsMapper.findGoodDescById(id);

        return goodsDesc;
    }
}
