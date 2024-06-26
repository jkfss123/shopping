package com.lingshi.shopping_manager_api.controller;

import com.lingshi.common.entity.Brand;
import com.lingshi.common.result.BaseResult;
import com.lingshi.common.service.IBrandService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jkl
 * @since 2024-06-26
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    @DubboReference(timeout = 5000)
    private IBrandService brandService;


    @GetMapping("/findById")
    public BaseResult findById(Long id){
        Brand brand = brandService.findById(id);
        return BaseResult.success(brand);
    }
}
