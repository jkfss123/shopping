package com.lingshi.shopping_goods_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingshi.shopping_common.entity.Specification;

import java.util.List;

public interface SpecificationMapper extends BaseMapper<Specification> {
    Specification findById(Long id);

    List<Specification> findByProductTypeId(Long productTypeId);
}
