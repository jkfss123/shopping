package com.lingshi.shopping_manager_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.common.entity.Admin;
import com.lingshi.common.result.BaseResult;
import com.lingshi.common.service.IAdminService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @DubboReference
    private IAdminService adminService;

    /**
     * 新增管理员
     * @param admin
     * @return
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody Admin admin){
        adminService.add(admin);
        return BaseResult.success();
    }

    /**
     * 修改管理员
     * @param admin
     * @return
     */
    @PutMapping("/update")
    public BaseResult update(@RequestBody Admin admin){
        adminService.update(admin);
        return BaseResult.success();
    }

    /**
     * 删除管理员
     * @param aid 管理员idd
     * @return
     */
    @DeleteMapping("/delete")
    public BaseResult delete(Long aid){
        adminService.delete(aid);
        return BaseResult.success();
    }

    /**
     * 根据id查询
     * @param aid 主键id
     * @return
     */
    @GetMapping("/findById")
    public BaseResult findByIdd(Long aid){
        Admin admin = adminService.findById(aid);
        return BaseResult.success(admin);
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页条数
     * @return
     */
    @GetMapping("/search")
    public BaseResult search(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer size){
        Page<Admin> search = adminService.search(page,size);
        return BaseResult.success(search);
    }
    @PutMapping("/updateRoleToAdmin")
    public BaseResult updateRoleToAdmin(Long aid,Long[] rids){
        adminService.updateRoleAdmin(aid,rids);
        return BaseResult.success();
    }
}
