package com.lb.fulinshou.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * order订单表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order")
public class Order {
    //id
    @TableId(type = IdType.AUTO)
    private Long orderId;

    //生成的订单编号
    private String orderNo;

    //订单描述：商品id*商品数量
    private String orderDescribe;

    //订单的预支付交易会话标识
    private String orderPrepayId;

    //订单总价，单位分
    private int orderPrice;

    //订单中的地址id
    private Long orderReceiptId;

    //订单备注
    private String orderRemarks;

    //订单所属用户id
    private Long orderUserId;

    //订单状态
    private String orderStatus;

    //订单创建时间
    private Date createTime;

    //订单修改时间
    private Date updateTime;


}
