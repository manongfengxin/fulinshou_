package com.lb.fulinshou.controller;

import com.lb.fulinshou.pojo.Receipt;
import com.lb.fulinshou.pojo.dto.MyOrder;
import com.lb.fulinshou.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 我的界面的一些接口
 */
@RestController
@RequestMapping("/my")
public class MyController {
    @Resource
    private MyService myService;

    /**
     * 添加用户地址信息
     * @param receipt
     */
    @PostMapping("/addReceipt/{userId}")
    public void addReceipt(@PathVariable Long userId, @RequestBody Receipt receipt){
        myService.addReceipt(userId, receipt);
    }

    /**
     * 修改用户地址信息
     * @param receipt
     */
    @PutMapping("/putReceipt")
    public void putReceipt(@RequestBody Receipt receipt){
        myService.putReceipt(receipt);
    }

    /**
     * 查询用户下的所有地址信息
     * @param receiptUserId
     * @return
     */
    @GetMapping("/getReceipt/{receiptUserId}")
    public List<Receipt> getReceipt(@PathVariable Long receiptUserId){
        List<Receipt> receiptList = myService.getReceipt(receiptUserId);

        return receiptList;
    }

    /**
     * 根据用户id查询自己名下的所有订单
     * @param orderUserId
     * @return
     */
    @GetMapping("/getOrder/{orderUserId}")
    public List<MyOrder> getOrder(@PathVariable Long orderUserId){
        List<MyOrder> myOrderList = myService.getOrder(orderUserId);

        return myOrderList;
    }

    /**
     * 获取用户名下所有已支付订单
     * @param userId
     * @return
     */
    /*@GetMapping("/getOrdersReceive/{userId}")
    public List<MyOrder> getOrdersReceive(@PathVariable Long userId){
        List<MyOrder> myOrderList = myService.getOrdersReceive(userId);

        return myOrderList;
    }*/

    /**
     * 获取名下所有未支付订单
     * @param userId
     * @return
     */
    /*@GetMapping("/getOrdersNotReceive/{userId}")
    public List<MyOrder> getOrdersNotReceive(@PathVariable Long userId){
        List<MyOrder> myOrderList = myService.getOrdersNotReceive(userId);

        return myOrderList;
    }*/
}
