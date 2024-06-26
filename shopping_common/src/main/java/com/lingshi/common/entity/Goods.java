package com.lingshi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author jkl
 * @since 2024-06-26
 */
@Getter
@Setter
@TableName("t_goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    @TableField("goodsName")
    private String goodsName;

    /**
     * 副标题
     */
    @TableField("caption")
    private String caption;

    /**
     * 价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 品牌id
     */
    @TableField("brandId")
    private Long brandId;

    /**
     * 一级类目
     */
    @TableField("productType1Id")
    private Long productType1Id;

    /**
     * 二级类目
     */
    @TableField("productType2Id")
    private Long productType2Id;

    /**
     * 三级类目
     */
    @TableField("productType3Id")
    private Long productType3Id;

    /**
     * 头图
     */
    @TableField("headerPic")
    private String headerPic;

    /**
     * 商品介绍
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 是否上架
     */
    @TableField("isMarketable")
    private Boolean isMarketable;
}
