package com.lingshi.shopping_goods_service.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.entity.Brand;
import com.lingshi.shopping_common.exception.BusCodeEnum;
import com.lingshi.shopping_common.exception.BusException;
import com.lingshi.shopping_common.service.IBrandService;
import com.lingshi.shopping_goods_service.mapper.BrandMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@DubboService
@Transactional
public class BrandServiceImpl implements IBrandService {
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public void add(Brand brand) {
        brandMapper.insert(brand);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.updateById(brand);
    }

    @Override
    public void delete(Long id) {
        //判断是否商品关联
        //TODO（待办任务） 判断是否商品关联

        brandMapper.deleteById(id);
    }

    @Override
    public Page<Brand> search(Brand brand, int page, int size) {
        LambdaQueryWrapper<Brand> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(brand) && StrUtil.isNotBlank(brand.getName())){
            lambdaQueryWrapper.like(Brand::getName,brand.getName());
        }
        Page<Brand> brandPage = brandMapper.selectPage(new Page<>(page,size),lambdaQueryWrapper);

        return brandPage;
    }

    @Override
    public List<Brand> findAll() {
        return brandMapper.selectList(null);
    }

    @Override
    public Brand findById(Long id) {
        Brand brand = brandMapper.selectById(id);
        if(id < 1){
//            throw new BusException(BusCodeEnum.SYSTEM_ERROR);
            BusException.busException(BusCodeEnum.SYSTEM_ERROR);
        }

        return brand;
    }
}
