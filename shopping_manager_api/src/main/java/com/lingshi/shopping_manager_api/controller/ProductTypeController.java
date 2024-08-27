package com.lingshi.shopping_manager_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.entity.ProductType;
import com.lingshi.shopping_common.service.result.BaseResult;
import com.lingshi.shopping_common.service.IProductTypeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * 商品类型管理
 */
@RestController
@RequestMapping("/productType")
public class ProductTypeController {

    @DubboReference
    private IProductTypeService productTypeService;

    /**
     * 新增商品类型
     * @param productType
     * @return
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody ProductType productType) {
        productTypeService.add(productType);
        return BaseResult.success();
    }

    /**
     * 修改商品类型
     * @param productType
     * @return
     */
    @PutMapping("/update")
    public BaseResult update(@RequestBody ProductType productType) {
        productTypeService.update(productType);
        return BaseResult.success();
    }

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public BaseResult delete(Long id) {
        productTypeService.delete(id);
        return BaseResult.success();
    }

    /**
     * 主键id查询
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public BaseResult  findById(Long id){
        return BaseResult.success(productTypeService.findById(id));
    }

    /**
     * 分页条件查询
     * @param productType
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    public BaseResult search(ProductType productType,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer size){
        Page<ProductType> search = productTypeService.search(productType, page, size);
        return BaseResult.success(search);
    }

    /**
     * 条件查询
     * @param productType
     * @return
     */
    @GetMapping("/findProductType")
    public BaseResult findProductType(ProductType productType){
        return BaseResult.success(productTypeService.findProductType(productType));
    }

    @GetMapping("/findByParentId")
    public BaseResult findParentId(Long parentId){
        return BaseResult.success(productTypeService.findByParentId(parentId));
    }
}
