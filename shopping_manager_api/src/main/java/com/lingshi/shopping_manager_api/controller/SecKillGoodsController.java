package com.lingshi.shopping_manager_api.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.entity.SeckillGoods;
import com.lingshi.shopping_common.service.result.BaseResult;
import com.lingshi.shopping_common.service.ISecKillGoodsService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * 秒杀商品
 */
@RestController
@RequestMapping("/seckillGoods")
public class SecKillGoodsController {
    @DubboReference
    private ISecKillGoodsService secKillGoodsService;


    /**
     * 添加秒杀商品
     * @param seckillGoods 秒杀商品实体
     * @return 操作结果
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody SeckillGoods seckillGoods) {
        secKillGoodsService.add(seckillGoods);
        return BaseResult.success();
    }


    /**
     * 修改秒杀商品
     * @param seckillGoods 秒杀商品实体
     * @return 操作结果
     */
    @PutMapping("/update")
    public BaseResult update(@RequestBody SeckillGoods seckillGoods) {
        secKillGoodsService.update(seckillGoods);
        return BaseResult.success();
    }


    /**
     * 分页查询秒杀商品
     * @param page 页数
     * @param size 每页条数
     * @return 查询结果
     */
    @GetMapping("/findPage")
    public BaseResult findPage(@RequestParam(defaultValue = "1")Integer page,
                             @RequestParam(defaultValue = "10")Integer size){
        Page<SeckillGoods> findPage = secKillGoodsService.findPage(page,size);

        return BaseResult.success(findPage);
    }
}
