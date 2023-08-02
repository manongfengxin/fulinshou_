package com.lb.fulinshou.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * receipt收货地址表的实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receipt {
    @TableId(type = IdType.AUTO)//id自增长
    //收货信息id
    private Long receiptId;

    //收货姓名
    private String receiptName;

    //收货地址
    private String receiptAddress;

    //收货手机号
    private String receiptPhone;

    //收货信息所属用户id
    private Long receiptUserId;

/*    //是否为默认收货地址 1 true / 0 false
    private int receiptStatus;*/
}
