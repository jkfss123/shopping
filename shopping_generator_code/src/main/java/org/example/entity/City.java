package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 行政区域地州市信息表
 * </p>
 *
 * @author jkl
 * @since 2024-06-26
 */
@Getter
@Setter
@TableName("t_city")
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 城市ID
     */
    @TableField("id")
    private String id;

    /**
     * 城市名称
     */
    @TableField("city")
    private String city;

    /**
     * 省份ID
     */
    @TableField("provinceid")
    private String provinceid;
}
