package com.lb.fulinshou.service;

import com.lb.fulinshou.common.Result;
import com.lb.fulinshou.model.WXAuth;
import com.lb.fulinshou.pojo.dto.UserDto;

public interface UserService {
    Result getSessionId(String code);

    Result authLogin(WXAuth wxAuth);

    Result login(UserDto userDto);

    Result register(UserDto userDto);

//    Result userinfo(String token, Boolean refresh);
}
