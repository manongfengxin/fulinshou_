package com.lb.fulinshou.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * goods商品表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    //id
    @TableId(type = IdType.AUTO)
    private Long goodsId;

    //商品标题
    private String goodsTitle;

    //商品价格，单位分
    private int goodsPrice;

    //商品配方
    private String goodsFormula;

    //商品功效
    private String goodsEffect;

}
