package com.lb.fulinshou.pojo.dto;

import com.lb.fulinshou.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 给用户展示的商品信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyGoods {
    //id
    private Long goodsId;

    //商品标题
    private String goodsTitle;

    //商品价格，单位分
    private int goodsPrice;

    //商品数量
    private int goodsAmount;

    //商品图片，只选一张
    private String goodsPhoto;

    public MyGoods(Goods goods){
        this.goodsId = goods.getGoodsId();
        this.goodsTitle = goods.getGoodsTitle();
        this.goodsPrice = goods.getGoodsPrice();
    }
}
