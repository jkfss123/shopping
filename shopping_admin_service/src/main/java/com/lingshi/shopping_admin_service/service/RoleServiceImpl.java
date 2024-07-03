package com.lingshi.shopping_admin_service.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.common.entity.Role;
import com.lingshi.common.entity.RolePermission;
import com.lingshi.common.service.IRoleService;
import com.lingshi.shopping_admin_service.mapper.RoleMapper;
import com.lingshi.shopping_admin_service.mapper.RolePermissionMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public void add(Role role) {
        roleMapper.insert(role);

    }

    @Override
    public void delete(Long rid) {

        //1.删除角色
        roleMapper.deleteById(rid);
        //2.删除角色和权限中间表的数据
        LambdaUpdateWrapper<RolePermission> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RolePermission::getRid,rid);
        rolePermissionMapper.delete(updateWrapper);
    }

    @Override
    public void update(Role role) {
        roleMapper.updateById(role);
    }

    @Override
    public Role findById(Long rid) {
        Role role = roleMapper.findById(rid);
        return role;
    }

    @Override
    public Page<Role> search(int page, int size) {
        return roleMapper.selectPage(new Page<>(page,size),null);
    }

    @Override
    public void updatePermissionToRole(Long rid, Long[] pids) {
        //1.删除角色权限中间表的数据
        LambdaUpdateWrapper<RolePermission> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RolePermission::getRid,rid);
        rolePermissionMapper.delete(updateWrapper);

        //2.添加新的角色权限数据
        for (Long pid : pids){
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRid(rid);
            rolePermission.setPid(pid);
            rolePermissionMapper.insert(rolePermission);
        }

    }
}
