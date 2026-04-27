package com.prd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prd.entity.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PersonMapper extends BaseMapper<Person> {

    void batchUpdatePositions(@Param("list") List<Person> persons);
}