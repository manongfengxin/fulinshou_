package com.lb.fulinshou.task;

import com.lb.fulinshou.pojo.Order;
import com.lb.fulinshou.service.impl.PayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class PayTask {
    @Resource
    private PayServiceImpl payServiceImpl;

    /**
     * 从第0秒开始，每隔30秒执行一次，查询创建超过1分钟，并且未支付的订单
     *
     * 微信流程图上说的是前端调用后端接口，但这里我采用定时任务去查询订单
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void orderConfirm() throws Exception{
        log.info("orderConfirm被执行");

        //查询创建超过1分钟并且未支付的订单
        List<Order> orderList = payServiceImpl.getNoPayOrderByDuration(1);

        for (Order order : orderList) {
            String orderNo = order.getOrderNo();//获取订单编号
            log.info("超时订单 订单号 ==> {}", orderNo);

            //核实订单状态
            payServiceImpl.checkOrderStatus(orderNo);
        }
    }
}
