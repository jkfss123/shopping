package com.lingshi.shopping_admin_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingshi.common.entity.Admin;

public interface AdminMapper extends BaseMapper<Admin> {

    Admin findById(Long aid);
}
