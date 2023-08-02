package com.lb.fulinshou.service.impl;

import com.lb.fulinshou.mapper.CarouselMapper;
import com.lb.fulinshou.mapper.HomeMapper;
import com.lb.fulinshou.pojo.Carousel;
import com.lb.fulinshou.pojo.Home;
import com.lb.fulinshou.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    @Resource
    private HomeMapper homeMapper;

    @Resource
    private CarouselMapper carouselMapper;

    /**
     * 获取首页轮播图信息
     * @return
     */
    @Override
    public List<Carousel> getCarousel() {
        List<Carousel> carouselList = carouselMapper.selectCarousel();

        return carouselList;
    }

    /**
     * 获取首页展示信息
     * @return
     */
    @Override
    public List<Home> getHome() {
        List<Home> homeList = homeMapper.selectHome();

        return homeList;
    }
}
