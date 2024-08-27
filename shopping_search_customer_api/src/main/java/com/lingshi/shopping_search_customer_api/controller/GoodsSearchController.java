package com.lingshi.shopping_search_customer_api.controller;

import com.lingshi.shopping_common.entity.GoodsSearchParam;
import com.lingshi.shopping_common.entity.GoodsSearchResult;
import com.lingshi.shopping_common.service.result.BaseResult;
import com.lingshi.shopping_common.service.IGoodsSearchService;
import com.lingshi.shopping_common.service.IGoodsService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/goodsSearch")
public class GoodsSearchController {

    @DubboReference
    private IGoodsSearchService goodsSearchService;

    @DubboReference
    private IGoodsService goodsService;



    /**
     * 自动补全
     * @param keyword 关键字
     * @return 补全结果
     */
    @GetMapping("/autoSuggest")
    public BaseResult autoSuggest(String keyword){
        List<String> suggestResult = goodsSearchService.autoSuggest(keyword);
        return BaseResult.success(suggestResult);
    }

    /**
     * 商品搜索
     * @param goodsSearchParam 搜索参数
     * @return 搜索结果
     */
    @PostMapping("/search")
    public BaseResult search(@RequestBody GoodsSearchParam goodsSearchParam){
        GoodsSearchResult goodsSearchResult = goodsSearchService.search(goodsSearchParam);
        return BaseResult.success(goodsSearchResult);
    }

    /**
     * 查询商品明细
     * @param id 商品id
     * @return 商品明细
     */
    @GetMapping("/findDesc")
    public BaseResult findDesc(Long id){
        return BaseResult.success(goodsService.findGoodsDescById(id));
    }
}