package com.prd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prd.entity.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GroupMapper extends BaseMapper<Group> {

    @Update("UPDATE person SET group_id = NULL WHERE group_id = #{groupId}")
    void clearGroupMembers(@Param("groupId") Long groupId);
}
