package com.lb.fulinshou.service;

import com.lb.fulinshou.pojo.Carousel;
import com.lb.fulinshou.pojo.Home;

import java.util.List;

public interface HomeService {
    List<Carousel> getCarousel();

    List<Home> getHome();
}
