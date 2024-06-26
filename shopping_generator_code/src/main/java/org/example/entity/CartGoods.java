package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

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
@TableName("t_cart_goods")
public class CartGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    @TableField("goodId")
    private Long goodId;

    /**
     * 商品名称
     */
    @TableField("goodsName")
    private String goodsName;

    /**
     * 单价
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 头图
     */
    @TableField("headerPic")
    private String headerPic;

    /**
     * 购买数量
     */
    @TableField("num")
    private Integer num;

    /**
     * 属于的订单id
     */
    @TableField("orderId")
    private String orderId;
}
