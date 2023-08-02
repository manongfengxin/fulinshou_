package com.lb.fulinshou.controller;

import com.lb.fulinshou.pojo.ShoppingCar;
import com.lb.fulinshou.pojo.dto.ShoppingCarDto;
import com.lb.fulinshou.service.ShoppingCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: fengxin
 * @CreateTime: 2023-07-30  19:52
 * @Description: 购物车页面
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCar")
public class ShoppingCarController {

    @Autowired
    private ShoppingCarService shoppingCarService;

    /**
     * @description: 添加购物车
     * @author: fengxin
     * @date: 2023/7/30 19:03
     * @param: user_id, goods_id, goods_number
     * @return: 成功
     **/
    @PostMapping("/add")
    public String addShoppingCar(@RequestBody ShoppingCar shoppingCar) {
        log.info("调用addShoppingCar接口，前端传入数据：{}",shoppingCar);
        if (shoppingCar.getUserId() == null) {
           return "添加购物车失败";
        }
        shoppingCarService.addShoppingCar(shoppingCar.getUserId(),shoppingCar.getGoodsId(),shoppingCar.getGoodsNumber());
        return "添加购物车成功";
    }

    /**
     * @description: 通过用户id查询用户的购物车列表
     * @author: fengxin
     * @date: 2023/7/31 10:48
     * @param: [userId]
     * @return: java.util.List<com.lb.fulinshou.pojo.dto.ShoppingCarDto>
     **/
    @GetMapping("/getList")
    public List<ShoppingCarDto> getShoppingCarList(@RequestParam("userId") Long userId) {
        log.info("调用getShoppingCarList接口，前端传入数据：{}",userId);
        if (userId == null) {
            return null;
        }
        return shoppingCarService.getShoppingCarList(userId);
    }

    /**
     * @description: 修改购物车商品数量
     * @author: fengxin
     * @date: 2023/7/31 12:49
     * @param: [userId, goodsId, goodsNumber]
     * @return: void
     **/
    @PutMapping("/update")
    public String updateGoodsNumber(@RequestBody ShoppingCar shoppingCar) {
        log.info("调用updateGoodsNumber接口，前端传入数据：{}",shoppingCar);
        if (shoppingCar.getUserId() == null) {
            return "修改商品数量失败";
        }
        return shoppingCarService.updateGoodsNumber(shoppingCar.getUserId(),shoppingCar.getGoodsId(),shoppingCar.getGoodsNumber());
    }

    /**
     * @description: 删除指定的购物车商品
     * @author: fengxin
     * @date: 2023/7/31 12:49
     * @param: [userId，goodsId]
     * @eturn: void
     **/
    @DeleteMapping("/delete")
    public String deleteShoppingCar(@RequestBody ShoppingCar shoppingCar) {
        log.info("调用deleteShoppingCar接口，前端传入数据：{}",shoppingCar);
        if (shoppingCar.getUserId() == null) {
            return "删除指定商品失败";
        }
        return shoppingCarService.deleteShoppingCar(shoppingCar.getUserId(), shoppingCar.getGoodsId());
    }

    /**
     * @description: 清空购物车（所有）
     * @author: fengxin
     * @date: 2023/7/31 15:57
     * @param: [userId]
     * @return: void
     **/
    @DeleteMapping("/deleteAll")
    public String deleteShoppingCarAll(@RequestParam("userId")Long userId) {
        log.info("调用deleteShoppingCar接口，前端传入数据：{}",userId);
        if (userId == null) {
            return "清空购物车失败";
        }
        return shoppingCarService.deleteShoppingCarAll(userId);
    }

}
