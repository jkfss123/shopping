package com.lingshi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

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
@TableName("t_admin")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "aid", type = IdType.AUTO)
    private Long aid;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    /**
     * exist=false 不是数据库字段，在mybatisplus在操作忽略
     */
    @TableField(exist = false)
    private List<Role> roles;
}
