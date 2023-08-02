package com.lb.fulinshou.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: fengxin
 * @CreateTime: 2023-07-30  20:15
 * @Description: TODO
 */
@Data
public class ShoppingCarDto {

    // 商品id
    private Long goodsId;

    // 商品名称
    private String goodsName;

    // 商品首图
    private String goodsImage;

    // 商品单价
    private double goodsPrice;

    // 商品数量
    private int goodsNumber;

    // 构造函数（商品单价单位改为：分）
    public ShoppingCarDto(Long goodsId, String goodsName, String goodsImage, int goodsPrice, int goodsNumber) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsImage = goodsImage;
        this.goodsPrice = Double.parseDouble(String.valueOf(goodsPrice)) / 100;
        this.goodsNumber = goodsNumber;
    }
}
