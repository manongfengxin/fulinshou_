package com.lb.fulinshou.controller;

import com.lb.fulinshou.common.Result;
import com.lb.fulinshou.model.WXAuth;
import com.lb.fulinshou.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 登录的一些接口
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserServiceImpl userServiceImpl;

    /**
     * 根据code获取session_key
     * @param code
     * @return
     */
    @GetMapping("/getSessionId")
    public Result getSessionId(String code){
        log.info("获取session_key接口被调用");

        return userServiceImpl.getSessionId(code);
    }

    /**
     * 用户登录
     * @param wxAuth
     * @return
     */
    @PostMapping("/authLogin")
    public Result authLogin(@RequestBody WXAuth wxAuth) {
        log.info("用户登录接口被调用，wxauth ==> {}",wxAuth);
        Result result = userServiceImpl.authLogin(wxAuth);
//        log.info("result ==> {}",result);
        return result;
    }
}
