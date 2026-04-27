package com.prd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prd.entity.Relationship;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RelationshipMapper extends BaseMapper<Relationship> {

    @Select("SELECT * FROM relationship WHERE from_person_id = #{personId} OR to_person_id = #{personId}")
    List<Relationship> findByPersonId(@Param("personId") Long personId);
}
