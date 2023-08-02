package com.lb.fulinshou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lb.fulinshou.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 针对order表的操作
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 根据用户id查询order信息
     * @param orderUserId
     * @return
     */
    @Select("select all from order where order_user_id = #{orderUserId}}")
    public List<Order> selectByUserId(Long orderUserId);

    /**
     * 根据订单编号获取订单状态
     * @param orderNo
     * @return
     */
    @Select("select order_status from order where order_no = #{orderNo}")
    public String selectOrderStatusByOrderNo(String orderNo);

}
