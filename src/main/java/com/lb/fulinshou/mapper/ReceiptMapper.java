package com.lb.fulinshou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lb.fulinshou.pojo.Receipt;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 针对receipt表的操作
 */
@Mapper
public interface ReceiptMapper extends BaseMapper<Receipt> {
    /**
     * 添加地址信息
     * @param receipt
     * @param receiptUserId
     */
    @Insert("insert into receipt set receipt_name = #{receiptName}, receipt_address = #{receiptAddress}, receipt_phone = #{receiptPhone}, receipt_user_id = #{receiptUserId}")
    public void insertReceipt(Receipt receipt, long receiptUserId);

    /**
     * 修改地址信息
     * @param receipt
     */
    @Update("update receipt set receipt_name = #{receiptName}, receipt_address = #{receiptAddress}, receipt_phone = #{receiptPhone} where receipt_id = #{receiptId}")
    public void updateReceipt(Receipt receipt);

    /**
     * 根据用户id查找地址信息
     * @param receiptUserId
     * @return
     */
    @Select("select receipt_id, receipt_name, receipt_address, receipt_phone, receipt_user_id from receipt where receipt_user_id = #{receiptUserId}")
    public List<Receipt> selectReceiptByReceiptUserId(long receiptUserId);

    /**
     * 根据id查找地址信息
     * @param addressId
     * @return
     */
/*    @Select("select address_content from address where address_id = #{addressId}")
    public String selectReceiptByAddressId(long addressId);*/
}
