package com.lingshi.shopping_user_customer_api.controller;


import cn.hutool.core.util.RandomUtil;
import com.lingshi.shopping_common.entity.ShoppingUser;
import com.lingshi.shopping_common.service.result.BaseResult;
import com.lingshi.shopping_common.service.IMessageService;
import com.lingshi.shopping_common.service.IShoppingUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * 商城用户
 */
@RestController
@RequestMapping("/user/shoppingUser")
public class ShoppingUserController {
    @DubboReference
    private IShoppingUserService userService;
    @DubboReference
    private IMessageService messageService;
    /**
     * 发送注册短信
     * @param phone 注册手机号
     * @return 操作结果
     */
    @GetMapping("/sendMessage")
    public BaseResult sendMessage(String phone) {
        //生成4为随机码
        String code = RandomUtil.randomNumbers(4);
        //发送短信
        BaseResult result = messageService.sendMessage(phone, code);
        if (result.getCode() == 200) {
            userService.saveRegisterCheckCode(phone, code);
            return BaseResult.success();
        }
        return result;
    }

    /**
     * 注册校验验证码
     * @param phone 手机号
     * @param checkCode 验证码
     * @return
     */
    @GetMapping("/registerCheckCode")
    public BaseResult registerCheckCode(String phone, String checkCode) {
        userService.registerCheckCode(phone, checkCode);
        return BaseResult.success();
    }

    /**
     * 注册
     * @param shoppingUser 用户对象
     * @return
     */
    @PostMapping("/register")
    public BaseResult register(@RequestBody ShoppingUser shoppingUser) {
        userService.register(shoppingUser);
        return BaseResult.success();
    }

    /**
     * 账号密码登录
     * @param shoppingUser 用户对象
     * @return
     */
    @PostMapping("/loginPassword")
    public BaseResult loginPassword(@RequestBody ShoppingUser shoppingUser) {
        String token = userService.loginPassword(shoppingUser.getUsername(), shoppingUser.getPassword());
        return BaseResult.success(token);
    }

    /**
     * 手机验证码登录
     * @param phone 手机号
     * @return
     */
    @GetMapping("/sendLoginCheckCode")
    public BaseResult sendLoginCheckCode(String phone) {
        String code = RandomUtil.randomNumbers(4);
        //发送短信
        BaseResult result = messageService.sendMessage(phone, code);
        if (200 == result.getCode()) {
            userService.saveLoginCheckCode(phone, code);
            return BaseResult.success();
        }
        return result;
    }

    /**
     * 手机验证码登录
     * @param phone 手机号
     * @param checkCode 验证码
     * @return
     */
    @PostMapping("/loginCheckCode")
    public BaseResult loginCheckCode(String phone,String checkCode){
        String token = userService.loginCheckCode(phone, checkCode);
        return BaseResult.success(token);
    }

    /**
     * 登录成以后获取用户名
     * @param authorization
     * @return
     */
    @GetMapping("/getName")
    public BaseResult getName(@RequestHeader("Authorization") String authorization){
        String token = authorization.replace("Bearer ","");
        String username = userService.getName(token);
        return BaseResult.success(username);
    }
}
