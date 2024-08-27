package com.lingshi.shopping_category_customer_api;

import com.lingshi.shopping_common.service.result.BaseResult;
import com.lingshi.shopping_common.service.ICategoryService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/category")
public class CategoryController {
    @DubboReference
    private ICategoryService categoryService;

    @RequestMapping("/all")
    public BaseResult all(){
        return BaseResult.success(categoryService.findAll());
    }
}
