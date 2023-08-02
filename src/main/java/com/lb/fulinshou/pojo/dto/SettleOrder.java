package com.lb.fulinshou.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 前端传递过来的订单信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettleOrder {
    //地址id
    private Long receiptId;

    //商品id列表
    private List<Long> goodsIdList;

    //商品数量列表（要与id列表对应)
    private List<Integer> goodsAmountList;

    //用户id
    private Long userId;

    //备注
    private String remarks;
}
