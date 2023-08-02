package com.lb.fulinshou.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * user用户表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @TableId(type = IdType.AUTO)//指定id自增长
    private Long id;

    private String nickName;

    private String userName;

    private String password;
    private String gender;

    /**
     * 头像
     */
    private String portrait;


    private String phoneNumber;

    private String openId;
}
