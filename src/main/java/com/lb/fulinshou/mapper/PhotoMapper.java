package com.lb.fulinshou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lb.fulinshou.pojo.Photo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 针对photo表的操作
 */
@Mapper
public interface PhotoMapper extends BaseMapper<Photo> {
    /**
     * 根据图片所属的商品id查找图片路径列表
     * @param photoGoodsId
     * @return
     */
    @Select("select photo_url from photo where photo_goods_id = #{photoGoodsId}")
    public List<String> selectPhotoListByPhotoGoodsId(long photoGoodsId);

    /**
     * 根据图片所属的商品id查找一张图片
     * @param photoGoodsId
     * @return
     */
    @Select("select photo_url from photo where photo_goods_id = #{photoGoodsId} limit 1")
    public String selectPhotoByPhotoGoodsId(long photoGoodsId);
}
