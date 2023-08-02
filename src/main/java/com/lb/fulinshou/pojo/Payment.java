package com.lb.fulinshou.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * payment交易表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("payment")
public class Payment {
    //id
    @TableId(type = IdType.AUTO)
    private Long id;

    //订单编号
    private String orderNo;

    //微信支付返回的支付id
    private String transactionId;

    //支付类型
    private String tradeType;

    //支付状态
    private String tradeStatus;

    //支付者标识
    private String openId;

    //用户支付金额
    private int payerTotal;

    //微信返回的全部支付信息
    private String content;

    //支付单创建时间
    private Date createTime;

    //支付单修改时间
    private Date updateTime;
}
