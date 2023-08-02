package com.lb.fulinshou.service;

import com.lb.fulinshou.pojo.dto.ShoppingCarDto;

import java.util.List;

/**
 * @Author: fengxin
 * @CreateTime: 2023-07-30  19:29
 * @Description: TODO
 */
public interface ShoppingCarService {

    // 添加购物车
    void addShoppingCar(Long userId, Long goodsId, int goodsNumber);

    // 获取购物车列表
    List<ShoppingCarDto> getShoppingCarList(Long userId);

    // 修改指定商品在购物车中的数量
    String updateGoodsNumber(Long userId, Long goodsId, int goodsNumber);

    // 删除购物车中指定商品
    String deleteShoppingCar(Long userId, Long goodsId);

    // 清空购物车
    String deleteShoppingCarAll(Long userId);
}
