package com.lb.fulinshou.controller;

import com.lb.fulinshou.mapper.ShoppingCarMapper;
import com.lb.fulinshou.pojo.Goods;
import com.lb.fulinshou.pojo.ShoppingCar;
import com.lb.fulinshou.pojo.dto.TGoods;
import com.lb.fulinshou.service.GoodsService;
import com.lb.fulinshou.service.MyService;
import com.lb.fulinshou.service.ShoppingCarService;
import com.lb.fulinshou.service.impl.SettleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 商品界面的接口
 */
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    @Autowired
    private ShoppingCarService shoppingCarService;

    /**
     * 获取商品页面侧边栏信息
     * @return
     */
    @GetMapping("/getGoodsTitle")
    public List<Goods> getGoodsTitle(){
        log.info("getGoodsTitle 被执行");
        List<Goods> goodsTitleList = goodsService.getGoodsTitle();

        return goodsTitleList;
    }

    /**
     * 根据前端传来商品id获取商品详细信息
     * @param goodsId
     * @return
     */
    @GetMapping("/getTGoods/{goodsId}")
    public TGoods getTGoods(@PathVariable Long goodsId){
        log.info("getTGoods 被执行");
        TGoods tGoods = goodsService.getTGoods(goodsId);

        return tGoods;
    }

}
