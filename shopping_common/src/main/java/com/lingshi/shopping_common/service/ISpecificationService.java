package com.lingshi.shopping_common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.entity.Specification;
import com.lingshi.shopping_common.entity.SpecificationOptions;

import java.util.List;

public interface ISpecificationService {
    void add(Specification specification);

    void update(Specification specification);

    Specification findById(Long id);

    void delete(Long id);

    List<Specification> findByProductTypeId(Long productTypeId);

    Page<Specification> search(int page,int size);

    void addOption(SpecificationOptions specificationOptions);

    void deleteOption(Long[] ids);
}
