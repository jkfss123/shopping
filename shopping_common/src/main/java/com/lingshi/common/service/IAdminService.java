package com.lingshi.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.common.entity.Admin;

import java.util.List;

public interface IAdminService {
    /**
     * 新增管理员
     */
    void add(Admin admin);

    /**
     * 根据id删除
     * @param id
     */
    void delete(Long id);

    /**
     * 修改管理员
     * @param admin
     */
    void update(Admin admin);

    /**
     * 主键id查询
     * @param id
     * @return
     */
    Admin findById(Long id);

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<Admin> search(int page,int size);

    /**
     * 查询所有
     */
    List<Admin> findAll();

    /**
     * 修改管理员角色
     * @param aid 用户
     * @param rid 角色集合
     */
    void updateRoleAdmin(Long aid,Long[] rid);
}
