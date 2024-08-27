package com.lingshi.shopping_goods_service;

import com.lingshi.shopping_common.entity.GoodsDesc;
import com.lingshi.shopping_common.service.IGoodsSearchService;
import com.lingshi.shopping_common.service.IGoodsService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GoodsTest {

    @Autowired
    private IGoodsService goodsService;

    @DubboReference
    private IGoodsSearchService goodsSearchService;

    @Test
    void testFindAll(){
        List<GoodsDesc> all = goodsService.findAll();
        for (GoodsDesc goodsDesc : all){
            System.out.println("goodsDesc = " + goodsDesc);
            //同步数据到ES
            goodsSearchService.syncGoodsToES(goodsDesc);
        }
    }
}
