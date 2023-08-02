package com.lb.fulinshou.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    carousel首页轮播图表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carousel {
    @TableId(type = IdType.AUTO)
    //首页轮播图id
    private Long carouselId;

    //轮播图图片url
    private String carouselPhoto;

}
