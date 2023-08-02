package com.lb.fulinshou.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    商品图片表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
    //id
    @TableId(type = IdType.AUTO)
    private Long photoId;

    //商品图片url
    private String photoUrl;

    //商品图片所属商品id
    private Long photoGoodsId;
}
