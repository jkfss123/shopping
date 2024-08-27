package com.lingshi.shopping_search_service.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GoodsSearchServiceImplTest {

    @Autowired
    private  GoodsSearchServiceImpl goodsSearchService;

    @Test
    void analyzer() {
        List<String> result = goodsSearchService.analyzer("我是中国人，华为手机", "ik_pinyin");
        System.out.println("goodsSearchService = " + result);
    }

    @Test
    void autoSuggest(){
        List<String> list = goodsSearchService.autoSuggest("i");
        System.out.println("list = " + list);

    }
}