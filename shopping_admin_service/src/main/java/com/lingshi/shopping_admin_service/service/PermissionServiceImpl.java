package com.lingshi.shopping_admin_service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.common.entity.Permission;
import com.lingshi.common.entity.RolePermission;
import com.lingshi.common.exception.BusCodeEnum;
import com.lingshi.common.exception.BusException;
import com.lingshi.common.service.IPermissionService;
import com.lingshi.shopping_admin_service.mapper.PermissionMapper;
import com.lingshi.shopping_admin_service.mapper.RolePermissionMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public void add(Permission permission) {
        permissionMapper.insert(permission);
    }

    @Override
    public void update(Permission permission) {
        permissionMapper.updateById(permission);
    }

    @Override
    public void delete(Long pid) {
        //1.判断权限是否有角色关联使用
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getPid,pid);
        Long count = rolePermissionMapper.selectCount(queryWrapper);
        if (count>0){
            BusException.busException(BusCodeEnum.PARAMETER_ERROR);
        }else {
            permissionMapper.deleteById(pid);
        }
    }

    @Override
    public Permission findById(Long pid) {
        return permissionMapper.selectById(pid);
    }

    @Override
    public Page<Permission> search(int page, int size) {
        return permissionMapper.selectPage(new Page<>(page,size),null);
    }

    @Override
    public List<Permission> findAll() {
        List<Permission> permissions = permissionMapper.selectList(null);
        return permissions;
    }
}
