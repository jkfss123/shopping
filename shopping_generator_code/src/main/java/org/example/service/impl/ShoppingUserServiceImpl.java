package org.example.service.impl;

import org.example.entity.ShoppingUser;
import org.example.mapper.ShoppingUserMapper;
import org.example.service.IShoppingUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jkl
 * @since 2024-06-26
 */
@Service
public class ShoppingUserServiceImpl extends ServiceImpl<ShoppingUserMapper, ShoppingUser> implements IShoppingUserService {

}
