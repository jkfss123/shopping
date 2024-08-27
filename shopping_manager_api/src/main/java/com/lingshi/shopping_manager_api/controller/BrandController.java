package com.lingshi.shopping_manager_api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lingshi.shopping_common.entity.Brand;
import com.lingshi.shopping_common.service.result.BaseResult;
import com.lingshi.shopping_common.service.IBrandService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 根据主键id查询
     * @param id
     * @return
     */
    @GetMapping("/findById")
    @SaCheckPermission("/brand/findById")
    public BaseResult findById(Long id) {
        Brand brand = brandService.findById(id);
        return BaseResult.success(brand);
    }

    /**
     * 添加品牌
     * @param brand
     * @return
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody Brand brand) {
        brandService.add(brand);
        return BaseResult.success();
    }

    /**
     * 修改品牌
     * @param brand
     * @return
     */
    @PostMapping("/update")
    public BaseResult update(@RequestBody Brand brand) {
        brandService.update(brand);
        return BaseResult.success();
    }

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public BaseResult delete(Long id) {
        brandService.delete(id);
        return BaseResult.success();
    }

    /**
     * 分页条件查询
     * @param brand
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    public BaseResult search(@RequestBody Brand brand,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer size) {
        return BaseResult.success(brandService.search(brand,page,size));
    }

    /**
     * 查询所有
     * @return
     */
    @GetMapping("/all")
    public BaseResult findAll(){
        return BaseResult.success(brandService.findAll());
    }
}
