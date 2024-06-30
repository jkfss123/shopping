package com.lingshi;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient//注册得到注册中心注解
@EnableDubbo//注册dubbo服务注解
@SpringBootApplication
@MapperScan("com.lingshi.shopping_admin_service.mapper")
@RefreshScope //动态刷新
@Slf4j
public class ShoppingAdminServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingAdminServiceApplication.class, args);
        log.info("=============管理员服务启动成功==============");
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;

    }
}
