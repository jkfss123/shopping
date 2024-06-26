package com.lingshi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
@TableName("t_goods_image")
public class GoodsImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 图片标题
     */
    @TableField("imageTitle")
    private String imageTitle;

    /**
     * 图片路径
     */
    @TableField("imageUrl")
    private String imageUrl;

    /**
     * 商品id
     */
    @TableField("goodsId")
    private Long goodsId;
}
