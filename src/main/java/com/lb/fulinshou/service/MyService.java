package com.lb.fulinshou.service;

import com.lb.fulinshou.pojo.Receipt;
import com.lb.fulinshou.pojo.dto.MyOrder;

import java.util.List;

public interface MyService {
    void addReceipt(Long userId, Receipt receipt);

    void putReceipt(Receipt address);

    List<Receipt> getReceipt(Long receiptUserId);

    List<MyOrder> getOrder(Long orderUserId);

   /* List<MyOrder> getOrdersReceive(Long userId);

    List<MyOrder> getOrdersNotReceive(Long userId);*/
}
