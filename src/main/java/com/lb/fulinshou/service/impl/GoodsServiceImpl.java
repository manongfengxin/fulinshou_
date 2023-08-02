package com.lb.fulinshou.service.impl;

import com.lb.fulinshou.mapper.GoodsMapper;
import com.lb.fulinshou.mapper.PhotoMapper;
import com.lb.fulinshou.pojo.Goods;
import com.lb.fulinshou.pojo.dto.TGoods;
import com.lb.fulinshou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private PhotoMapper photoMapper;

    /**
     * 从数据库中获取goods商品表中的全部标题title
     * @return
     */
    @Override
    public List<Goods> getGoodsTitle() {
        List<Goods> goodsTitleList = goodsMapper.selectGoodsTitle();

        return goodsTitleList;
    }

    /**
     * 根据商品id从数据库中获取指定的商品信息
     * @param goodsId
     * @return
     */
    @Override
    public TGoods getTGoods(Long goodsId) {
        //首先根据id在商品表中查询到除商品图片的其余信息
        Goods goods = goodsMapper.selectGoodsById(goodsId);

        //将Goods信息封装到TGoods中
        TGoods tGoods = new TGoods(goods);

        //然后再去商品图片表中根据id查询到本商品的图片集合，并将其包装到TGoods对象中
        tGoods.setTgoodsPhotoList(photoMapper.selectPhotoListByPhotoGoodsId(tGoods.getTgoodsId()));

        //然后返回TGoods对象
        return tGoods;
    }

}
