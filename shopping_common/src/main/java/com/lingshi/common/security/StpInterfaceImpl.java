package com.lingshi.common.security;

import cn.dev33.satoken.stp.StpInterface;
import com.lingshi.common.entity.Permission;
import com.lingshi.common.service.IAdminService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StpInterfaceImpl implements StpInterface {

    @DubboReference
    private IAdminService adminService;
    /**
     * 获取当前登录用户所有的权限，如果进行权限校验使用
     * @param loginId 已经登录的用户id（就是账号）
     * @param loginType
     * @return 权限集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {

        List<Permission> allPermission = adminService.findAllPermission((String) loginId);

        List<String> permissions = allPermission.stream().map(Permission::getUrl).collect(Collectors.toList());

        System.out.println("permissions = " + permissions);

        return permissions;
    }

    /**
     * 获取当前登录用户，如果进行角色校验
     * @param o
     * @param s
     * @return
     */

    @Override
    public List<String> getRoleList(Object o, String s) {
        return null;
    }
}
