package com.lingshi.shopping_admin_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingshi.common.entity.Admin;
import com.lingshi.common.entity.Permission;

import java.util.List;

public interface AdminMapper extends BaseMapper<Admin> {

    Admin findById(Long aid);

    List<Permission> findAllPermission(String username);
}
