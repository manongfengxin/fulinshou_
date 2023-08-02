package com.lb.fulinshou.controller;

import cn.hutool.json.JSONObject;
import com.lb.fulinshou.pojo.Receipt;
import com.lb.fulinshou.pojo.dto.SettleGoods;
import com.lb.fulinshou.pojo.dto.SettleOrder;
import com.lb.fulinshou.service.impl.SettleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 结算界面的接口
 */
@RestController
@RequestMapping("/settle")
public class SettleController {
    @Resource
    private SettleServiceImpl settleServiceImpl;

    /**
     * 获取用户的所有收货地址信息
     */
    @GetMapping("/getReceipt/{receiptUserId}")
    public List<Receipt> getReceipt(@PathVariable Long receiptUserId){
        List<Receipt> receiptList = settleServiceImpl.getReceipt(receiptUserId);
        return receiptList;
    }

    /**
     * 根据商品id获取商品的信息
     */
    @GetMapping("/getSettleGoods/{goodsId}")
    public SettleGoods getSettleGoods(@PathVariable Long goodsId){
        SettleGoods settleGoods = settleServiceImpl.getSettleGoods(goodsId);

        return settleGoods;
    }

    /**
     * 根据前段传递的订单信息列表插入订单，并且返回前端调起支付请求的参数
     */
    @PostMapping("/addOrder")
    public JSONObject addOrder(@RequestBody SettleOrder settleOrder) throws IOException {
        return settleServiceImpl.addOrder(settleOrder);
    }


}
