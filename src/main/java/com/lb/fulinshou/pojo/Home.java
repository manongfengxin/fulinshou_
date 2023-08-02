package com.lb.fulinshou.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * home首页表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Home {
    //id
    @TableId(type = IdType.AUTO)
    private Long homeId;

    //首页展示图片url
    private String homePhoto;

    //首页内容介绍
    private String homeContent;

    //首页标题
    private String homeTitle;
}
