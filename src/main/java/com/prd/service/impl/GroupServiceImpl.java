package com.prd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.prd.entity.Group;
import com.prd.mapper.GroupMapper;
import com.prd.service.GroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    @Override
    @Transactional
    public boolean deleteGroup(Long id) {
        // 将分组内成员的 group_id 置空
        baseMapper.clearGroupMembers(id);
        // 删除分组
        return removeById(id);
    }
}
