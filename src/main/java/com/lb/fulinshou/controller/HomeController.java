package com.lb.fulinshou.controller;

import com.lb.fulinshou.pojo.Carousel;
import com.lb.fulinshou.pojo.Home;
import com.lb.fulinshou.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 首页的一些接口
 */
@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {
    @Resource
    private HomeService homeService;

    /**
     * 获取首页轮播图展示数据
     * @return
     */
    @GetMapping("/getCarousel")
    public List<Carousel> getCarousel(){
        log.info("getCarousel 被执行");

        List<Carousel> carouselList = homeService.getCarousel();

        return carouselList;
    }

    /**
     * 获取首页展示信息
     */
    @GetMapping("/getHome")
    public List<Home> getHome(){
        log.info("getHome 被执行");
        List<Home> homeList = homeService.getHome();

        return homeList;
    }
}
