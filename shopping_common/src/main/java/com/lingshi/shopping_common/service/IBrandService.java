package com.lingshi.shopping_common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.entity.Brand;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jkl
 * @since 2024-06-26
 */
public interface IBrandService {

    void add(Brand brand);
    void update(Brand brand);
    void delete(Long id);
    Page<Brand> search(Brand brand,int page,int size);

    List<Brand> findAll();


    Brand findById(Long id);

}
