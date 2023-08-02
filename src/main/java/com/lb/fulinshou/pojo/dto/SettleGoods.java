package com.lb.fulinshou.pojo.dto;

import com.lb.fulinshou.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 传递到结算界面的商品信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettleGoods {
    //id
    private Long settleGoodsId;

    //商品标题
    private String settleGoodsTitle;

    //商品单价
    private Double settleGoodsPrice;

    //商品图片（只一个）
    private String settleGoodsPhoto;

    public SettleGoods(Goods goods){
        this.settleGoodsId = goods.getGoodsId();
        this.settleGoodsTitle = goods.getGoodsTitle();
        this.settleGoodsPrice = Double.parseDouble(String.valueOf(goods.getGoodsPrice())) / 100;
    }
}
