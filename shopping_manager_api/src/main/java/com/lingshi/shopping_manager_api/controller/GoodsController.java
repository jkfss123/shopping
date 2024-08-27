package com.lingshi.shopping_manager_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.entity.Goods;
import com.lingshi.shopping_common.service.result.BaseResult;
import com.lingshi.shopping_common.service.IGoodsService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * 商品管理。
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @DubboReference
    private IGoodsService goodsService;

    /**
     * 新增商品
     * @param goods
     * @return
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody Goods goods){
        goodsService.add(goods);
        return BaseResult.success(goods);
    }

    /**
     * 修改
     * @param goods
     * @return
     */
    @PutMapping("/update")
    public BaseResult update(@RequestBody Goods goods){
        goodsService.update(goods);
        return BaseResult.success(goods);
    }

    /**
     * 商品上下架
     * @param id
     * @param isMarketable
     * @return
     */
    @PutMapping("/putAway")
    public BaseResult putAway(Long id,Boolean isMarketable){
        goodsService.putAway(id,isMarketable);
        return BaseResult.success();
    }

    /**
     * 分页查询
     * @param goods
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    public BaseResult search(Goods goods,
                             @RequestParam(defaultValue = "1")Integer page,
                             @RequestParam(defaultValue = "10")Integer size) {
        Page<Goods> search = goodsService.search(goods,page,size);
        return BaseResult.success(search);
    }
}
