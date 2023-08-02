package com.lb.fulinshou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lb.fulinshou.mapper.GoodsMapper;
import com.lb.fulinshou.mapper.PhotoMapper;
import com.lb.fulinshou.mapper.ShoppingCarMapper;
import com.lb.fulinshou.pojo.Goods;
import com.lb.fulinshou.pojo.ShoppingCar;
import com.lb.fulinshou.pojo.dto.ShoppingCarDto;
import com.lb.fulinshou.service.ShoppingCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: fengxin
 * @CreateTime: 2023-07-30  19:31
 * @Description: TODO
 */
@Slf4j
@Service
public class ShoppingCarServiceImpl implements ShoppingCarService {

    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private PhotoMapper photoMapper;

    /**
     * @description: 添加购物车
     * @author: fengxin
     * @date: 2023/7/30 19:33
     * @param: [userId, goodsId, goodsNumber]
     * @return: void
     **/
    @Override
    public void addShoppingCar(Long userId, Long goodsId, int goodsNumber) {
        // 获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = simpleDateFormat.format(System.currentTimeMillis());
        ShoppingCar shoppingCar = new ShoppingCar(userId,goodsId,goodsNumber,nowTime);
        // 判断购物车内是否已有该商品
        QueryWrapper<ShoppingCar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id",goodsId);
        ShoppingCar shoppingCarInfo = shoppingCarMapper.selectOne(queryWrapper);
        if (shoppingCarInfo != null) { // 如果购物车已经包含了该商品
            // 修改数量信息
            shoppingCarInfo.setGoodsNumber(shoppingCar.getGoodsNumber() + shoppingCarInfo.getGoodsNumber());
            shoppingCarMapper.updateById(shoppingCarInfo);
        }else { // 如果购物车未包含了该商品
            // 添加购物车
            shoppingCarMapper.insert(shoppingCar);
        }


    }

    /**
     * @description: 获取指定用户的购物车列表
     * @author: fengxin
     * @date: 2023/7/30 20:12
     * @param: [userId]
     * @return: java.util.List<com.lb.fulinshou.pojo.ShoppingCar>
     **/
    @Override
    public List<ShoppingCarDto> getShoppingCarList(Long userId) {
        // 通过用户id查询购物车列表（单条数据包括：购物车id、商品id、商品数量、用户id、创建时间）
        QueryWrapper<ShoppingCar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<ShoppingCar> shoppingCarList = shoppingCarMapper.selectList(queryWrapper);
        // 通过商品id查询指定商品的id、名称、首图、单价
        List<ShoppingCarDto> shoppingCarDtos = new ArrayList<>();
        for (ShoppingCar shoppingCar : shoppingCarList) {
            // 拿到商品id
            Long goodsId = shoppingCar.getGoodsId();
            // 通过商品id查询指定商品的id、名称、单价
            Goods goods = goodsMapper.selectOtherGoodsById(goodsId);
            // 通过商品id查询指定商品首图
            String goodsImage = photoMapper.selectPhotoByPhotoGoodsId(goodsId);

            // 填充返回前端信息：商品id、商品名称、商品首图、商品单价、商品数量
            ShoppingCarDto shoppingCarDto =
                    new ShoppingCarDto(goodsId, goods.getGoodsTitle(),
                            goodsImage, goods.getGoodsPrice(), shoppingCar.getGoodsNumber());
//            shoppingCarDtos.add(shoppingCarDto);

            // 根据前端需要（方便数据处理），按照商品数量将一条数据分为“商品数量”条数据发送给前端
            int count = shoppingCarDto.getGoodsNumber();
            shoppingCarDto.setGoodsNumber(1);
            while (count > 1) {
                shoppingCarDtos.add(shoppingCarDto);
                count--;
            }
            shoppingCarDtos.add(shoppingCarDto);
        }
        log.info("获取购物车列表数据条数：{}",shoppingCarDtos.size());
        return shoppingCarDtos;
    }

    /**
     * @description: 通过商品id修改购物车中对应商品的数量
     * @author: fengxin
     * @date: 2023/7/31 11:01
     * @param: [goodsId, goodsNumber]
     * @return: void
     **/
    @Override
    public String updateGoodsNumber(Long userId, Long goodsId, int goodsNumber) {
        // 构建修改信息
        ShoppingCar shoppingCar = new ShoppingCar();
        shoppingCar.setGoodsNumber(goodsNumber);
        // 构建条件信息
        QueryWrapper<ShoppingCar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id",goodsId).eq("user_id",userId);
        // 修改
        shoppingCarMapper.update(shoppingCar, queryWrapper);
        return "数量修改成功";
    }

    /*
     * @description: 通过商品id删除指定商品
     * @author: fengxin
     * @date: 2023/7/31 11:17
     * @param: [goodsId]
     * @return: void
     **/
    @Override
    public String deleteShoppingCar(Long userId, Long goodsId) {
        // 构建条件
        QueryWrapper<ShoppingCar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id",goodsId).eq("user_id", userId);
        // 删除
        shoppingCarMapper.delete(queryWrapper);
        return "删除指定商品成功";
    }

    @Override
    public String deleteShoppingCarAll(Long userId) {
        // 构建条件
        QueryWrapper<ShoppingCar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        // 删除
        shoppingCarMapper.delete(queryWrapper);
        return "清除购物车成功";
    }


}
