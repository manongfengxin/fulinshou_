package com.lb.fulinshou.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 给用户展示的订单信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyOrder {
    //id
    private Long orderId;

    //订单编号
    private String orderNo;

    //订单中的商品
    private List<MyGoods> myGoodsList;

    //订单总价格
    private Double orderPrice;

    //订单备注
    private String orderRemarks;

    //订单所属用户id
    private Long orderUserId;

    //订单状态
    private String orderStatus;

    //订单创建时间——但这里选择的是订单修改时间
    private Date updateTime;
}
