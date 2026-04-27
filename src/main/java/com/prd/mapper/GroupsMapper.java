package com.prd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prd.entity.Groups;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GroupsMapper extends BaseMapper<Groups> {

    @Update("UPDATE person SET group_id = NULL WHERE group_id = #{groupId}")
    void clearGroupMembers(@Param("groupId") Long groupId);

    @Update("UPDATE `groups` SET position_x = #{positionX}, position_y = #{positionY} WHERE id = #{id}")
    void updatePosition(Groups group);
}
