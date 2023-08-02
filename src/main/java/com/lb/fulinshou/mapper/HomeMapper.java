package com.lb.fulinshou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lb.fulinshou.pojo.Home;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 针对home表的操作
 */
@Mapper
public interface HomeMapper extends BaseMapper<Home> {
    @Select("select home_id, home_photo, home_content, home_title from home")
    public List<Home> selectHome();
}
