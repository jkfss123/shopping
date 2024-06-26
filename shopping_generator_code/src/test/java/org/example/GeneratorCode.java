package org.example;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class GeneratorCode {


    public static void main(String[] args) {
        // 使用 FastAutoGenerator 快速配置代码生成器
        FastAutoGenerator.create("jdbc:mysql://192.168.44.134:3312/shopping?serverTimezone=GMT%2B8", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("jkl") // 设置作者
                            .outputDir("shopping_generator_code/src/main/java"); // 输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("org.example") // 设置父包名
                            .entity("entity") // 设置实体类包名
                            .mapper("mapper") // 设置 Mapper 接口包名
                            .service("service") // 设置 Service 接口包名
                            .serviceImpl("service.impl") // 设置 Service 实现类包名
                            .xml("mappers"); // 设置 Mapper XML 文件包名
                })


                .strategyConfig(builder -> {
                    builder.addInclude(
                                    "t_address",
                                    "t_admin",
                                    "t_admin_role",
                                    "t_area",
                                    "t_brand",
                                    "t_cart_goods",
                                    "t_category",
                                    "t_city",
                                    "t_goods",
                                    "t_goods_image",
                                    "t_goods_specification_option",
                                    "t_orders",
                                    "t_payment",
                                    "t_permission",
                                    "t_product_type",
                                    "t_province",
                                    "t_role",
                                    "t_role_permission",
                                    "t_seckill_goods",
                                    "t_shopping_user",
                                    "t_specification",
                                    "t_specification_option"
                            ) // 设置需要生成的表名
                            .addTablePrefix("t_")
                            .entityBuilder()
                            .enableLombok() // 启用 Lombok
                            .enableTableFieldAnnotation() // 启用字段注解
                            .controllerBuilder()
                            .enableRestStyle(); // 启用 REST 风格
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用 Freemarker 模板引擎
                .execute(); // 执行生成
    }
}
