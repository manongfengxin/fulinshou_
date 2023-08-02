package com.lb.fulinshou.controller;

import com.lb.fulinshou.pojo.Receipt;
import com.lb.fulinshou.service.CartService;
import com.lb.fulinshou.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 购物车界面的一些接口
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Resource
    private CartService cartService;

    @Resource
    private MyService myService;

    /**
     * 根据前端传递的用户id查找获取购物车信息
     * @param userId
     * @return
     */
    /*@GetMapping("/getMyCarts/{userId}")
    public List<MyCart> getMyCarts(@PathVariable Long userId){
        List<MyCart> myCartList = cartService.getMyCarts(userId);

        return myCartList;
    }*/

    /**
     * 根据前端传递的订单信息来添加订单
     * @param orderList
     */
    /*@PostMapping("/addOrder")
    public void addOrder(@RequestBody List<Order> orderList){
        for (Order order : orderList) {
            cartService.addOrder(order);
        }
    }
*/
    /**
     * 将所有信息汇总返回
     * @param myCartList
     * @param addressId
     * @return
     */
    /*@GetMapping("/getSettle/{addressId}")
    public List<Settle> getSettle(@RequestBody List<MyCart> myCartList, @PathVariable Long addressId){
        List<Settle> settleList = new ArrayList<>();
        for (MyCart myCart : myCartList) {
            settleList.add(cartService.getSettle(myCart.getMyCartId(), myCart.getMyCartNumbers(), addressId));
        }

        return settleList;
    }*/


    /**
     * 获取用户名下所有地址信息
     * @param receiptUserId
     */
    @GetMapping("/getReceipt/{receiptUserId}")
    public List<Receipt> getReceipt(@PathVariable Long receiptUserId){
        List<Receipt> receiptList = myService.getReceipt(receiptUserId);

        return receiptList;
    }

}
