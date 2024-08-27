package com.lingshi.shopping_goods_service.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.entity.ProductType;
import com.lingshi.shopping_common.exception.BusException;
import com.lingshi.shopping_common.service.IProductTypeService;
import com.lingshi.shopping_goods_service.mapper.ProductTypeMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

import static com.lingshi.shopping_common.exception.BusCodeEnum.DELETE_PRODUCT_TYPE_ERROR;
import static com.lingshi.shopping_common.exception.BusCodeEnum.INSERT_OR_PRODUCT_TYPE_ERROR;

@DubboService
public class ProductTypeTypeServiceImpl implements IProductTypeService {

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Override
    public void add(ProductType productType) {
        //查询父1i是否有对应的分类
        ProductType parent = productTypeMapper.selectById(productType.getParentId());
        if (Objects.isNull(parent)) {
            productType.setLevel(1);
        } else if (parent.getLevel() < 3){
            productType.setLevel(parent.getLevel() + 1);
    } else if(parent.getLevel() == 3){
            BusException.busException(INSERT_OR_PRODUCT_TYPE_ERROR);
        }
        productTypeMapper.insert(productType);

}

    @Override
    public void update(ProductType productType) {
        //查询父1i是否有对应的分类
        ProductType parent = productTypeMapper.selectById(productType.getParentId());
        if (Objects.isNull(parent)) {
            productType.setLevel(1);
        } else if (parent.getLevel() < 3){
            productType.setLevel(parent.getLevel() + 1);
        } else if(parent.getLevel() == 3){
            BusException.busException(INSERT_OR_PRODUCT_TYPE_ERROR);
        }
        productTypeMapper.updateById(productType);

    }

    @Override
    public ProductType findById(Long id) {
        return productTypeMapper.selectById(id);
    }

    @Override
    public void delete(Long id) {
        //判断是否有子分类，如果有不能删除
        LambdaQueryWrapper<ProductType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductType::getParentId,id);
        Long subCount = productTypeMapper.selectCount(queryWrapper);
        if (subCount>0) {
            BusException.busException(DELETE_PRODUCT_TYPE_ERROR);
        }
        productTypeMapper.deleteById(id);
    }

    @Override
    public Page<ProductType> search(ProductType productType, int page, int size) {
        LambdaQueryWrapper<ProductType> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(productType) && StrUtil.isNotBlank(productType.getName())){
            queryWrapper.like(ProductType::getName,productType.getName());
        }
        return productTypeMapper.selectPage(new Page<>(page,size),queryWrapper);
    }

    @Override
    public List<ProductType> findProductType(ProductType productType) {
        LambdaQueryWrapper<ProductType> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(productType) && StrUtil.isNotBlank(productType.getName())){
            queryWrapper.like(ProductType::getName,productType.getName());
        }
        return productTypeMapper.selectList(queryWrapper);
    }

    @Override
    public List<ProductType> findByParentId(Long parentId) {
        LambdaQueryWrapper<ProductType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductType::getParentId,parentId);
        return productTypeMapper.selectList(queryWrapper);
    }
}
