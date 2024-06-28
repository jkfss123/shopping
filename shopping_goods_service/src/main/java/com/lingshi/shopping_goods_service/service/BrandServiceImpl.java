package com.lingshi.shopping_goods_service.service;

import com.lingshi.common.entity.Brand;
import com.lingshi.common.result.BaseResult;
import com.lingshi.common.service.IBrandService;
import com.lingshi.shopping_goods_service.mapper.BrandMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@DubboService
@Transactional
public class BrandServiceImpl implements IBrandService {
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public Brand findById(Long id) {
        Brand brand = brandMapper.selectById(id);


        return brand;
    }
}
