package com.lingshi.shopping_manager_api.controller;

import com.lingshi.shopping_common.entity.Specification;
import com.lingshi.shopping_common.entity.SpecificationOptions;
import com.lingshi.shopping_common.service.result.BaseResult;
import com.lingshi.shopping_common.service.ISpecificationService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specification")
public class SpecificationController {

    @DubboReference
    private ISpecificationService specificationService;

    @PostMapping("/add")
    public BaseResult add(@RequestBody Specification specification){
        specificationService.add(specification);
        return BaseResult.success();
    }
    @PutMapping("/update")
    public BaseResult update(@RequestBody Specification specification){
        specificationService.update(specification);
        return BaseResult.success();
    }
    @DeleteMapping("/delete")
    public BaseResult delete(Long id){
        specificationService.delete(id);
        return BaseResult.success();
    }
    @GetMapping("/findById")
    public BaseResult findById(Long id){
        Specification specification = specificationService.findById(id);
        return BaseResult.success(specification);
    }
    @GetMapping("/findByProductTypeId")
    public BaseResult findByProductTypeId(Long id){
        List<Specification> specifications = specificationService.findByProductTypeId(id);
        return BaseResult.success(specifications);
    }
    @GetMapping("/search")
    public BaseResult search(@RequestParam(defaultValue = "1")Integer page,
                             @RequestParam(defaultValue = "10")Integer size){
        return BaseResult.success(specificationService.search(page,size));
    }
    @PostMapping("/addOption")
    public BaseResult add(@RequestBody SpecificationOptions options){
        specificationService.addOption(options);
        return BaseResult.success();
    }
    @DeleteMapping("deleteOption")
    public BaseResult deleteOption(Long[] ids){
        specificationService.deleteOption(ids);
        return BaseResult.success();
    }

}
