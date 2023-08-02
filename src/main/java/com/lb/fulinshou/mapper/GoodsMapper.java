package com.lb.fulinshou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lb.fulinshou.pojo.Goods;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 针对goods表的操作
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
    /**
     * 获取所有字段中的goods_id和goods_title
     */
    @Select("select goods_id, goods_title from goods")
    public List<Goods> selectGoodsTitle();

    /**
     * 根据商品id获取商品所有信息
     * @param goodsId
     * @return
     */
    @Select("select goods_id, goods_title, goods_price, goods_formula, goods_effect from goods where goods_id = #{goodsId}")
    public Goods selectGoodsById(long goodsId);

    /**
     * 根据商品id获取id，标题，价格的商品信息
     */
    @Select("select goods_id, goods_title, goods_price from goods where goods_id = #{goodsId}")
    public Goods selectOtherGoodsById(long goodsId);

    /**
     * 根据商品id获取商品的单价
     */
    @Select("select goods_price from goods where goods_id = #{goodsId}")
    public int selectPriceById(Long goodsId);


}
