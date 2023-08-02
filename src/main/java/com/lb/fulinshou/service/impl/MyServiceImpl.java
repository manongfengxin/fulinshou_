package com.lb.fulinshou.service.impl;

import com.lb.fulinshou.mapper.GoodsMapper;
import com.lb.fulinshou.mapper.OrderMapper;
import com.lb.fulinshou.mapper.PhotoMapper;
import com.lb.fulinshou.mapper.ReceiptMapper;
import com.lb.fulinshou.pojo.Goods;
import com.lb.fulinshou.pojo.Order;
import com.lb.fulinshou.pojo.Receipt;
import com.lb.fulinshou.pojo.dto.MyGoods;
import com.lb.fulinshou.pojo.dto.MyOrder;
import com.lb.fulinshou.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MyServiceImpl implements MyService {
    @Resource
    private ReceiptMapper receiptMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private PhotoMapper photoMapper;

    /**
     * 添加用户地址信息
     * @param userId
     * @param receipt
     */
    @Override
    public void addReceipt(Long userId, Receipt receipt) {
        receiptMapper.insertReceipt(receipt, userId);
    }

    /**
     * 修改用户的地址信息
     * @param receipt
     */
    @Override
    public void putReceipt(Receipt receipt) {
        receiptMapper.updateReceipt(receipt);
    }

    /**
     * 根据用户id查找用户名下所有地址信息
     * @param receiptUserId
     * @return
     */
    @Override
    public List<Receipt> getReceipt(Long receiptUserId) {
        List<Receipt> receiptList = receiptMapper.selectReceiptByReceiptUserId(receiptUserId);

        return receiptList;
    }

    /**
     * 根据用户id查询用户下所有订单
     * @param orderUserId
     * @return
     */
    @Override
    public List<MyOrder> getOrder(Long orderUserId) {
        //首先根据用户id获取Order表中的信息
        List<Order> orderList = orderMapper.selectByUserId(orderUserId);

        List<MyOrder> myOrderList = new ArrayList<>();

        for (Order order : orderList) {

            //MyOrder中的商品列表
            List<MyGoods> myGoodsList = new ArrayList<>();

            //获取订单中的商品描述
            String orderDescribe = order.getOrderDescribe();
            //然后从描述中取出订单中的商品id和商品数量描述
            String[] describes = orderDescribe.split("#");
            for (String describe : describes) {
                //再次分割，得到商品id和对应的商品数量
                String[] idAndAmount = describe.split("&");

                //用商品id查找到商品的部分信息
                Goods goods = goodsMapper.selectOtherGoodsById(Integer.parseInt(idAndAmount[0]));

                //再用商品id获取商品的第一张图片
                String goodsPhoto = photoMapper.selectPhotoByPhotoGoodsId(Integer.parseInt(idAndAmount[0]));

                //组装MyGoods
                MyGoods myGoods = new MyGoods((goods));
                myGoods.setGoodsAmount(Integer.parseInt(idAndAmount[1]));
                myGoods.setGoodsPhoto(goodsPhoto);

                //存入MyOrder中的商品列表
                myGoodsList.add(myGoods);
            }

            //组装MyOrder信息
            MyOrder myOrder = new MyOrder();
            myOrder.setOrderId(order.getOrderId());//id
            myOrder.setOrderNo(order.getOrderNo());//订单编号
            myOrder.setMyGoodsList(myGoodsList);//商品列表
            myOrder.setOrderPrice(Double.parseDouble(String.valueOf(order.getOrderPrice())) / 100);//先从int转成String，再转成Double，再除以100，因为Order中的价格单位是分
            myOrder.setOrderRemarks(order.getOrderRemarks());//订单备注
            myOrder.setOrderUserId(order.getOrderUserId());//订单所属用户id
            myOrder.setOrderStatus(order.getOrderStatus());//订单状态
            myOrder.setUpdateTime(order.getUpdateTime());//订单创建时间——但这里使用的是订单修改时间

            //添加到MyOrder列表中
            myOrderList.add(myOrder);
        }

        return myOrderList;
    }

    /**
     * 获取所有已收货订单
     * @param userId
     * @return
     */
    /*@Override
    public List<MyOrder> getOrdersReceive(Long userId) {
        List<Order> orderList = orderMapper.selectReceiveByOrderUserId(userId);

        List<MyOrder> myOrderList = new ArrayList<>();

        for (Order order : orderList) {
            //根据order中的商品id去查询商品信息
            Goods goods = goodsMapper.selectGoodsById(order.getOrderGoodsId());

            //根据goods中的地址id去查询地址信息
            String address = receiptMapper.selectAddressByAddressId(order.getOrderAddressId());


            MyOrder myOrder = new MyOrder(goods, address, order, userId);

            //求得myOrder总价格
            myOrder.setMyOrderPrice(goods.getGoodsPrice() * order.getOrderNumbers());

            myOrderList.add(myOrder);
        }

        return myOrderList;
    }*/

    /**
     * 获取所有未支付订单
     * @param userId
     * @return
     */
    /*@Override
    public List<MyOrder> getOrdersNotReceive(Long userId) {
        List<Order> orderList = orderMapper.selectNotReceiveByOrderUserId(userId);

        List<MyOrder> myOrderList = new ArrayList<>();

        for (Order order : orderList) {
            //根据order中的商品id去查询商品信息
            Goods goods = goodsMapper.selectGoodsById(order.getOrderGoodsId());

            //根据goods中的地址id去查询地址信息
            String address = receiptMapper.selectAddressByAddressId(order.getOrderAddressId());


            MyOrder myOrder = new MyOrder(goods, address, order, userId);

            //求得myOrder总价格
            myOrder.setMyOrderPrice(goods.getGoodsPrice() * order.getOrderNumbers());

            myOrderList.add(myOrder);
        }

        return myOrderList;
    }*/


}
