package com.lb.fulinshou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lb.fulinshou.pojo.Goods;
import com.lb.fulinshou.pojo.ShoppingCar;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 针对 shopping_car 表的操作
 */
@Mapper
public interface ShoppingCarMapper extends BaseMapper<ShoppingCar> {

}
