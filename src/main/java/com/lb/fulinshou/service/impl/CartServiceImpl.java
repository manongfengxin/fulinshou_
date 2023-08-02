package com.lb.fulinshou.service.impl;

import com.lb.fulinshou.mapper.GoodsMapper;
import com.lb.fulinshou.mapper.OrderMapper;
import com.lb.fulinshou.mapper.ReceiptMapper;
import com.lb.fulinshou.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CartServiceImpl implements CartService {
    /*@Autowired
    private CartMapper cartMapper;
*/
    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private ReceiptMapper receiptMapper;

    /**
     * 根据用户id查找购物车信息，并且包装出返回的实体类MyCart
     * @param userId
     * @return
     */
    /*@Override
    public List<MyCart> getMyCarts(Long userId) {
        //先用用户id去Cart表中查找状态为1的Cart信息
        List<Cart> cartList = cartMapper.selectByCartUserIdAndCartStatusTrue(userId);

        List<MyCart> myCartList = new ArrayList<>();

        //遍历cartList，包装出返回的实体类MyCart
        for (Cart cart : cartList) {
            //使用cart中的商品id去查找商品数据
            Goods goods = goodsMapper.selectGoodsById(cart.getCartGoodsId());
            //然后将cart和goods和用户id放入myCart中
            MyCart myCart = new MyCart(goods, cart, userId);

            //然后计算出总价格
            myCart.setMyCartPrice(goods.getGoodsPrice() * cart.getCartNumbers());

            myCartList.add(myCart);
        }

        return myCartList;
    }*/

    /**
     * 添加订单信息
     * @param order

     */
    /*@Override
    public void addOrder(Order order) {
        orderMapper.insertOrder(order);
    }*/

    /**
     * 将所有信息汇总返回
     * @param myCartId
     * @param myCartNumbers
     * @param addressId
     * @return
     */
    /*@Override
    public Settle getSettle(Long myCartId, int myCartNumbers, Long addressId) {
        //使用myCartId去查找cart的商品id和用户id
        List<Long> ids = cartMapper.selectCartGoodsIdAndCartUserIdByCartId(myCartId);

        //使用商品id去查找图片和标题
        Goods goods = goodsMapper.selectGoodsById(ids.get(0));

        //然后再使用myCartId去查找cart的数量cart_numbers
        int cartNumbers = cartMapper.selectCartNumbersByCartId(myCartId);

        //根据地址id去查找地址信息
        String address = receiptMapper.selectAddressByAddressId(addressId);

        //得到总价格
        Double price = goods.getGoodsPrice() * myCartNumbers;

        //将商品id，用户id，cart数量，地址，商品，价格加入到Settle中
        Settle settle = new Settle(ids.get(0), ids.get(1), myCartNumbers, address, addressId, goods, price);

        return settle;
    }*/



}
