package com.lb.fulinshou.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: fengxin
 * @CreateTime: 2023-07-30  18:59
 * @Description: 购物车
 */
@Data
@NoArgsConstructor
public class ShoppingCar {

    // 购物车id（自增）
    @TableId(type = IdType.AUTO)//id自增长
    private Long shoppingCarId;

    // 商品的id
    private Long goodsId;

    // 商品的数量
    private int goodsNumber;

    // 所属用户的id
    private Long userId;

    // 加入购物车的时间
    private String createTime;

    public ShoppingCar(Long userId, Long goodsId, int goodsNumber, String createTime) {
        this.goodsId = goodsId;
        this.goodsNumber = goodsNumber;
        this.userId = userId;
        this.createTime = createTime;
    }
}
