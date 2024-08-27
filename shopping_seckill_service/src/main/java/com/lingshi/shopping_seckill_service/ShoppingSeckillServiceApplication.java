package com.lingshi.shopping_seckill_service;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@EnableDubbo
@RefreshScope
@SpringBootApplication
@MapperScan("com.lingshi.shopping_seckill_service.mapper")
public class ShoppingSeckillServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(ShoppingSeckillServiceApplication.class, args);
    }

    @Bean
    public BitMapBloomFilter bitMapBloomFilter(){
        BitMapBloomFilter bitMapBloomFilter = new BitMapBloomFilter(1000);
        return bitMapBloomFilter;
    }
}
