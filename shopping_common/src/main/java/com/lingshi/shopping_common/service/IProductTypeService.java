package com.lingshi.shopping_common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.entity.ProductType;

import java.util.List;

public interface IProductTypeService {
    // 新增商品类型
    void add(ProductType productType);
    // 修改商品类型
    void update(ProductType productType);
    // 根据id查询商品类型
    ProductType findById(Long id);
    // 删除商品类型
    void delete(Long id);
    // 分页查询
    Page<ProductType> search(ProductType productType, int page, int size);
    // 根据条件查询商品类型
    List<ProductType> findProductType(ProductType productType);
    //根据父id查询子分类
    List<ProductType> findByParentId(Long parentId);
}
