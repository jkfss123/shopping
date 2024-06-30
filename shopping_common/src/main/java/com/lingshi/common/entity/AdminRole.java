package com.lingshi.common.entity;

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
@TableName("t_admin_role")
public class AdminRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("aid")
    private Long aid;

    @TableField("rid")
    private Long rid;
}
