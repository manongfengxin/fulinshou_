package com.lb.fulinshou.pojo.dto;

import com.lb.fulinshou.model.WxUserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

/**
 * 用于User和WxUserInfo之间进行转换的，因为User对应User表，而WxUserInfo对应微信登录信息，
 * 所以通过UserDto来对二者进行转换
 */
public class UserDto implements Serializable {

    private Long id;
    private String nickName;
    //更新的时候可以为null(代表不更新)
    private String userName;
    @NotNull
    private String password;
    private String gender;
    @NotNull
    private String phoneNumber;
//    /**
//     * 背景图片
//     */
//    private String background;
    private String portrait;

    private String openId;

//    private String wxUnionId;

    //dto拓展属性
    private String token;
    List<String> permissions;
    List<String> roles;
    //验证码
    private String code;

    public void from(WxUserInfo wxUserInfo) {
        this.nickName = wxUserInfo.getNickName();
        this.portrait = wxUserInfo.getAvatarUrl();
        this.userName = "";
        this.password = "";
        this.phoneNumber = "";
        this.gender = wxUserInfo.getGender();
        this.openId = wxUserInfo.getOpenId();
//        this.wxUnionId = wxUserInfo.getUnionId();
    }
}
