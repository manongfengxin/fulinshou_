package com.lb.fulinshou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lb.fulinshou.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 针对user表的操作
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select nick_name from user where id = #{id}")
    String selectNameById(Long id);

    @Select("select portrait from user where id = #{id}")
    String selectImgById(Long id);

    /**
     * 根据用户id查找用户的openid
     */
    @Select("select open_id from user where id = #{id}}")
    public String selectOpenIdById(Long id);
}
