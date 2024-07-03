package com.lingshi.shopping_manager_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.common.entity.Permission;
import com.lingshi.common.entity.Role;
import com.lingshi.common.result.BaseResult;
import com.lingshi.common.service.IPermissionService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @DubboReference
    private IPermissionService permissionService;

    @PostMapping("/add")
    public BaseResult add(@RequestBody Permission permission) {
        permissionService.add(permission);
        return BaseResult.success();
    }

    @PutMapping("/update")
    public BaseResult update(@RequestBody Permission permission) {
        permissionService.update(permission);
        return BaseResult.success();
    }

    @DeleteMapping("/delete")
    public BaseResult delete(Long pid) {
        permissionService.delete(pid);
        return BaseResult.success();
    }
    @GetMapping("/findById")
    public BaseResult findById(Long pid) {
        Permission permission = permissionService.findById(pid);
        return BaseResult.success(permission);
    }
    @GetMapping("/search")
    public BaseResult search(@RequestParam(defaultValue = "1")Integer page,
                             @RequestParam(defaultValue = "10")Integer size){
        Page<Permission> search = permissionService.search(page,size);
        return BaseResult.success(search);
    }
    @GetMapping("/findAll")
    public BaseResult findAll() {
        return BaseResult.success(permissionService.findAll());
    }
}
