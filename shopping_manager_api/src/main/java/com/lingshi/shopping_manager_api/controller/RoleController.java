package com.lingshi.shopping_manager_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.common.entity.Role;
import com.lingshi.common.result.BaseResult;
import com.lingshi.common.service.IRoleService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {


    @DubboReference
    private IRoleService roleService;

    /**
     * 新增角色
     * @param role
     * @return
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody Role role){
        roleService.add(role);
        return BaseResult.success();
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @PutMapping("/update")
    public BaseResult update(@RequestBody Role role){
        roleService.update(role);
        return BaseResult.success();
    }

    /**
     * 删除角色
     * @param rid
     * @return
     */
    @DeleteMapping("/delete")
    public BaseResult delete(Long rid){
        roleService.delete(rid);
        return BaseResult.success();
    }

    /**
     * 根据id查询角色
     * @param rid
     * @return
     */
    @GetMapping("/findById")
    public BaseResult findById(Long rid){
        Role role = roleService.findById(rid);
        return BaseResult.success(role);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    public BaseResult search(@RequestParam(defaultValue = "1")Integer page,
                             @RequestParam(defaultValue = "10")Integer size){
       Page<Role> search = roleService.search(page,size);
       return BaseResult.success(search);
    }

    /**
     * 修改角色的权限
     * @param rid
     * @param pids
     * @return
     */
    @PutMapping("/updatePermissionToRole")
    public BaseResult updatePermissionToRole(Long rid,Long[] pids){
        roleService.updatePermissionToRole(rid,pids);
        return BaseResult.success();
    }
}
