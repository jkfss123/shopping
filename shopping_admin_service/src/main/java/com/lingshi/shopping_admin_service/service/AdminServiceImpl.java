package com.lingshi.shopping_admin_service.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.common.entity.Admin;
import com.lingshi.common.entity.AdminRole;
import com.lingshi.common.result.BaseResult;
import com.lingshi.common.service.IAdminService;
import com.lingshi.shopping_admin_service.mapper.AdminMapper;
import com.lingshi.shopping_admin_service.mapper.AdminRoleMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DubboService
@Transactional
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Override
    public void add(Admin admin) {

        adminMapper.insert(admin);
    }

    @Override
    public void delete(Long aid) {
        //删除管理员
        adminMapper.deleteById(aid);
        //删除管理员角色中间表的数据
        LambdaUpdateWrapper<AdminRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AdminRole::getAid,aid);
        adminRoleMapper.delete(updateWrapper);
    }

    @Override
    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    @Override
    public Admin findById(Long aid) {

        return adminMapper.findById(aid);

    }

    @Override
    public Page<Admin> search(int page, int size) {

        return adminMapper.selectPage(new Page<>(page,size),null);


    }

    @Override
    public List<Admin> findAll() {
        return null;
    }

    @Override
    public void updateRoleAdmin(Long aid, Long[] rids) {

        //1.删除原有中间表的角色信息
        LambdaUpdateWrapper<AdminRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AdminRole::getAid,aid);
        adminRoleMapper.delete(updateWrapper);
        //2.添加新的角色信息
        for (Long rid : rids){
            //创建AdminRole
            AdminRole adminRole = new AdminRole();
            adminRole.setAid(aid);
            adminRole.setRid(rid);
            adminRoleMapper.insert(adminRole);
        }
    }
}
