package com.lingshi.shopping_user_service.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lingshi.shopping_common.constant.Const;
import com.lingshi.shopping_common.constant.RedisKey;
import com.lingshi.shopping_common.entity.ShoppingUser;
import com.lingshi.shopping_common.exception.BusCodeEnum;
import com.lingshi.shopping_common.exception.BusException;
import com.lingshi.shopping_common.service.IShoppingUserService;
import com.lingshi.shopping_user_service.mapper.ShoppingUserMapper;
import com.lingshi.shopping_user_service.util.JwtUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@DubboService
public class ShoppingUserServiceImpl implements IShoppingUserService {

    @Autowired
    private ShoppingUserMapper shoppingUserMapper;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveRegisterCheckCode(String phone, String checkCode) {
        //注册发送验证码之前，检查数据库是否有对应的手机号，如果有，说明已经注册，不需要再注册
        LambdaQueryWrapper<ShoppingUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingUser::getPhone, phone);
        Long count = shoppingUserMapper.selectCount(queryWrapper);
        if (count > 0) {
            BusException.busException(BusCodeEnum.REGISTER_PHONE_EXISTS);
        }

        //存储到redis
        String loginKey = StrUtil.format(RedisKey.USER_REGISTER_KEY, phone);
        stringRedisTemplate.opsForValue().set(loginKey, checkCode,Duration.ofMillis(5));
    }


    @Override
    public void registerCheckCode(String phone, String checkCode) {
        String registerKey = StrUtil.format(RedisKey.USER_REGISTER_KEY, phone);
        String redisCode = stringRedisTemplate.opsForValue().get(registerKey);
        if (!checkCode.equals(redisCode)) {
            BusException.busException(BusCodeEnum.VERIFY_CODE_ERROR);
        }
    }

    @Override
    public void register(ShoppingUser shoppingUser) {
        //检查账号是否存在
        LambdaQueryWrapper<ShoppingUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingUser::getUsername, shoppingUser.getUsername());
        Long count = shoppingUserMapper.selectCount(queryWrapper);
        if (count > 0) {
            BusException.busException(BusCodeEnum.REGISTER_USERNAME_EXISTS);
        }

        //密码加密
        String md5Password = SecureUtil.md5(shoppingUser.getPassword());
        shoppingUser.setPassword(md5Password);
        shoppingUser.setStatus(Const.Y);
        shoppingUserMapper.insert(shoppingUser);

    }

    @Override
    public String loginPassword(String username, String password) {
        LambdaQueryWrapper<ShoppingUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingUser::getUsername, username);
        String md5Password = SecureUtil.md5(password);
        queryWrapper.eq(ShoppingUser::getPassword, md5Password);
        ShoppingUser shoppingUser = shoppingUserMapper.selectOne(queryWrapper);
        if (Objects.isNull(shoppingUser)) {
            BusException.busException(BusCodeEnum.LOGIN_NAME_PASSWORD_ERROR);
        }
        //创建令牌
        String token = JwtUtils.sign(shoppingUser.getId(), shoppingUser.getUsername());
        return token;
    }

    @Override
    public void saveLoginCheckCode(String phone, String checkCode) {
        //检查账号是否存在
        LambdaQueryWrapper<ShoppingUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingUser::getPhone, phone);
        Long count = shoppingUserMapper.selectCount(queryWrapper);
        if (count == 0) {
            BusException.busException(BusCodeEnum.REGISTER_NOT_EXISTS);
        }

        String loginKey = StrUtil.format(RedisKey.USER_LOGIN_KEY, phone);
        stringRedisTemplate.opsForValue().set(loginKey, checkCode,Duration.ofMinutes(5));
    }

    @Override
    public String loginCheckCode(String phone, String checkCode) {
        String loginKey = StrUtil.format(RedisKey.USER_LOGIN_KEY, phone);
        String code = stringRedisTemplate.opsForValue().get(loginKey);
        if(!checkCode.equals(code)){
            BusException.busException(BusCodeEnum.VERIFY_CODE_ERROR);
        }

        // 登录成功，查询用户
        LambdaQueryWrapper<ShoppingUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingUser::getPhone, phone);
        ShoppingUser shoppingUser = shoppingUserMapper.selectOne(queryWrapper);

        //创建令牌
        String token = JwtUtils.sign(shoppingUser.getId(), shoppingUser.getUsername());
        return token;
    }

    @Override
    public String getName(String token) {
        Map<String, Object> result = JwtUtils.verify(token);
        if(CollUtil.isNotEmpty(result)){
            return (String)result.get("username");
        }
        return null;
    }

    @Override
    public ShoppingUser getLoginUser(Long id) {
        ShoppingUser shoppingUser = shoppingUserMapper.selectById(id);
        return shoppingUser;
    }
}