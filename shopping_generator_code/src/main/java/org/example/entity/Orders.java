package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@TableName("t_orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId("id")
    private String id;

    /**
     * 实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
     */
    @TableField("payment")
    private BigDecimal payment;

    /**
     * 支付方式  1、微信支付   2、支付宝支付
     */
    @TableField("paymentType")
    private String paymentType;

    /**
     * 邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分
     */
    @TableField("postFee")
    private String postFee;

    /**
     * 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
     */
    @TableField("status")
    private String status;

    /**
     * 订单创建时间
     */
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 付款时间
     */
    @TableField("paymentTime")
    private LocalDateTime paymentTime;

    /**
     * 发货时间
     */
    @TableField("consignTime")
    private LocalDateTime consignTime;

    /**
     * 交易完成时间
     */
    @TableField("endTime")
    private LocalDateTime endTime;

    /**
     * 交易关闭时间
     */
    @TableField("closeTime")
    private LocalDateTime closeTime;

    /**
     * 物流名称
     */
    @TableField("shippingName")
    private String shippingName;

    /**
     * 物流单号
     */
    @TableField("shippingCode")
    private String shippingCode;

    /**
     * 用户id
     */
    @TableField("userId")
    private Long userId;

    /**
     * 买家留言
     */
    @TableField("buyerMessage")
    private String buyerMessage;

    /**
     * 买家昵称
     */
    @TableField("buyerNick")
    private String buyerNick;

    /**
     * 收货地址
     */
    @TableField("receiverAreaName")
    private String receiverAreaName;

    /**
     * 收货人手机
     */
    @TableField("receiverMobile")
    private String receiverMobile;

    /**
     * 收货人邮编
     */
    @TableField("receiverZipCode")
    private String receiverZipCode;

    /**
     * 收货人
     */
    @TableField("receiver")
    private String receiver;

    /**
     * 订单过期时间
     */
    @TableField("expire")
    private LocalDateTime expire;
}
