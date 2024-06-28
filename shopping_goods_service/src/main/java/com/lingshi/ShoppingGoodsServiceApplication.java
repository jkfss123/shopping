package com.lingshi;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@EnableDiscoveryClient//注册得到注册中心注解
@EnableDubbo//注册dubbo服务注解
@SpringBootApplication
@MapperScan("com.lingshi.shopping_goods_service.mapper")
@RefreshScope //动态刷新
@Slf4j
public class ShoppingGoodsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingGoodsServiceApplication.class, args);
        log.info("==============商品微服务启动成功=============");

    }
}
