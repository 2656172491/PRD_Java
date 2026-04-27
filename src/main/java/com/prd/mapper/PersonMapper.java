package com.prd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prd.entity.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PersonMapper extends BaseMapper<Person> {

    @Update("<script>" +
            "<foreach collection='list' item='item' separator=';'>">" +
            "UPDATE person SET position_x = #{item.positionX}, position_y = #{item.positionY} WHERE id = #{item.id}" +
            "</foreach>" +
            "</script>")
    void batchUpdatePositions(@Param("list") List<Person> persons);
}
