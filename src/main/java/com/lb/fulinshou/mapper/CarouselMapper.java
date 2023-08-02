package com.lb.fulinshou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lb.fulinshou.pojo.Carousel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 针对carousel表的操作
 */
@Mapper
public interface CarouselMapper extends BaseMapper<Carousel> {
    @Select("select carousel_id, carousel_photo from carousel")
    public List<Carousel> selectCarousel();
}
