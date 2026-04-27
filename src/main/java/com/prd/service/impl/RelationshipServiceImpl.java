package com.prd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.prd.entity.Person;
import com.prd.entity.Relationship;
import com.prd.mapper.PersonMapper;
import com.prd.mapper.RelationshipMapper;
import com.prd.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RelationshipServiceImpl extends ServiceImpl<RelationshipMapper, Relationship> implements RelationshipService {

    private final PersonMapper personMapper;

    @Override
    @Transactional
    public Long createRelationship(Relationship relationship) {
        validateAndNormalize(relationship);

        LambdaQueryWrapper<Relationship> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Relationship::getFromPersonId, relationship.getFromPersonId())
                .eq(Relationship::getToPersonId, relationship.getToPersonId());
        List<Relationship> existing = list(wrapper);
        for (Relationship item : existing) {
            if (sameRelationTypes(item.getRelationTypes(), relationship.getRelationTypes())) {
                return item.getId();
            }
        }

        save(relationship);
        return relationship.getId();
    }

    @Override
    public void deleteRelationship(Long id) {
        removeById(id);
    }

    @Override
    public List<Relationship> findByPersonId(Long personId) {
        return baseMapper.findByPersonId(personId);
    }

    private void validateAndNormalize(Relationship relationship) {
        if (relationship == null
                || relationship.getFromPersonId() == null
                || relationship.getToPersonId() == null) {
            throw new IllegalArgumentException("关系的起止节点不能为空");
        }
        if (relationship.getFromPersonId().equals(relationship.getToPersonId())) {
            throw new IllegalArgumentException("不能创建指向自己的关系");
        }
        if (relationship.getRelationTypes() == null || relationship.getRelationTypes().isEmpty()) {
            throw new IllegalArgumentException("关系类型不能为空");
        }

        Person from = personMapper.selectById(relationship.getFromPersonId());
        Person to = personMapper.selectById(relationship.getToPersonId());
        if (from == null || to == null) {
            throw new IllegalArgumentException("关系节点不存在");
        }

        if (relationship.getFromPersonId() > relationship.getToPersonId()) {
            Long temp = relationship.getFromPersonId();
            relationship.setFromPersonId(relationship.getToPersonId());
            relationship.setToPersonId(temp);
        }
    }

    private boolean sameRelationTypes(List<String> left, List<String> right) {
        if (left == null || right == null) {
            return false;
        }
        Set<String> leftSet = new HashSet<>(left);
        Set<String> rightSet = new HashSet<>(right);
        return leftSet.equals(rightSet);
    }
}
