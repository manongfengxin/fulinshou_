package com.lb.fulinshou.pojo.dto;

import com.lb.fulinshou.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
    传递到前端的商品对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TGoods {
    //id
    private Long tgoodsId;

    //商品标题
    private String tgoodsTitle;

    //商品价格
    private Double tgoodsPrice;

    //商品配方
    private String tgoodsFormula;

    //商品功效
    private String tgoodsEffect;

    //商品图片集合
    private List<String> tgoodsPhotoList;

    //传递Goods对象封装到TGoods中
    public TGoods(Goods goods){
        this.tgoodsId = goods.getGoodsId();
        this.tgoodsTitle = goods.getGoodsTitle();
        this.tgoodsPrice = Double.parseDouble(String.valueOf(goods.getGoodsPrice())) / 100;
        this.tgoodsFormula = goods.getGoodsFormula();
        this.tgoodsEffect = goods.getGoodsEffect();
    }
}
