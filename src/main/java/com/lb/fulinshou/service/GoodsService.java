package com.lb.fulinshou.service;

import com.lb.fulinshou.pojo.Goods;
import com.lb.fulinshou.pojo.dto.TGoods;

import java.util.List;

public interface GoodsService {
    List<Goods> getGoodsTitle();

    TGoods getTGoods(Long goodsId);

}
