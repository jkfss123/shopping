package com.lingshi.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.common.entity.Role;

public interface IRoleService {

    /**
     * 新增
     * @param role
     */
    void add(Role role);

    /**
     * 删除
     * @param rid
     */
    void delete(Long rid);

    /**
     * 修改
     * @param role
     */
    void update(Role role);

    /**
     * 主键id查询
     * @param rid
     * @return
     */
    Role findById(Long rid);

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<Role> search(int page,int size);

    /**
     * 修改角色的权限
     * @param rid
     * @param pids
     */
    void updatePermissionToRole(Long rid,Long[] pids);
}
