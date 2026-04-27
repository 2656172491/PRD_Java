package com.prd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.prd.entity.Groups;
import com.prd.mapper.GroupsMapper;
import com.prd.service.GroupsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class GroupsServiceImpl extends ServiceImpl<GroupsMapper, Groups> implements GroupsService {

    @Override
    public Long createGroup(Groups groups) {
        validateGroup(groups);
        save(groups);
        return groups.getId();
    }

    @Override
    public void updateGroup(Long id, Groups groups) {
        validateGroup(groups);
        groups.setId(id);
        updateById(groups);
    }

    @Override
    @Transactional
    public boolean deleteGroup(Long id) {
        // 将分组内成员的 group_id 置空
        baseMapper.clearGroupMembers(id);
        // 删除分组
        return removeById(id);
    }

    private void validateGroup(Groups groups) {
        if (groups == null || !StringUtils.hasText(groups.getName())) {
            throw new IllegalArgumentException("分组名称不能为空");
        }
        groups.setName(groups.getName().trim());
    }
}
