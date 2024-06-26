package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 省份信息表
 * </p>
 *
 * @author jkl
 * @since 2024-06-26
 */
@Getter
@Setter
@TableName("t_province")
public class Province implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 省份ID
     */
    @TableId("id")
    private String id;

    /**
     * 省份名称
     */
    @TableField("provinceName")
    private String provinceName;
}
