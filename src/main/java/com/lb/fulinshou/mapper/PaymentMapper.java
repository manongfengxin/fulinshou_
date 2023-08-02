package com.lb.fulinshou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lb.fulinshou.pojo.Payment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 针对payemnt表的操作
 */
@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {
}
