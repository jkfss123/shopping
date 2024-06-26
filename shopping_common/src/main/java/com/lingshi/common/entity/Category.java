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
@TableName("t_category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 广告id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 广告标题
     */
    @TableField("title")
    private String title;

    /**
     * 广告链接
     */
    @TableField("url")
    private String url;

    /**
     * 图片地址
     */
    @TableField("pic")
    private String pic;

    /**
     * 广告状态 0:未启用 1:启用
     */
    @TableField("status")
    private Integer status;
}
