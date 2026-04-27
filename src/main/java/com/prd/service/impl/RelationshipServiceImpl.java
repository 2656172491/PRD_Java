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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelationshipServiceImpl extends ServiceImpl<RelationshipMapper, Relationship> implements RelationshipService {

    private final PersonMapper personMapper;

    @Override
    @Transactional
    public Long createRelationship(Relationship relationship) {
        validateAndNormalize(relationship);
        relationship.setIsVirtual(false);

        LambdaQueryWrapper<Relationship> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Relationship::getFromPersonId, relationship.getFromPersonId())
                .eq(Relationship::getToPersonId, relationship.getToPersonId())
                .eq(Relationship::getIsVirtual, false);
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
    @Transactional
    public Integer rebuildVirtualRelationships(Long selfPersonId) {
        if (selfPersonId == null) {
            throw new IllegalArgumentException("中心人物不能为空");
        }
        Person self = personMapper.selectById(selfPersonId);
        if (self == null) {
            throw new IllegalArgumentException("中心人物不存在");
        }

        LambdaQueryWrapper<Relationship> deleteVirtual = new LambdaQueryWrapper<>();
        deleteVirtual.eq(Relationship::getIsVirtual, true);
        remove(deleteVirtual);

        LambdaQueryWrapper<Relationship> realWrapper = new LambdaQueryWrapper<>();
        realWrapper.eq(Relationship::getIsVirtual, false);
        Set<String> realPairs = list(realWrapper)
                .stream()
                .map(item -> buildPairKey(item.getFromPersonId(), item.getToPersonId()))
                .collect(Collectors.toSet());

        List<Person> allPersons = personMapper.selectList(null);
        int createdCount = 0;
        for (Person person : allPersons) {
            if (person == null || person.getId() == null || person.getId().equals(selfPersonId)) {
                continue;
            }
            Long fromId = Math.min(selfPersonId, person.getId());
            Long toId = Math.max(selfPersonId, person.getId());
            if (realPairs.contains(buildPairKey(fromId, toId))) {
                continue;
            }

            Relationship virtualRel = new Relationship();
            virtualRel.setFromPersonId(fromId);
            virtualRel.setToPersonId(toId);
            virtualRel.setRelationTypes(List.of("虚拟关系"));
            virtualRel.setIsVirtual(true);
            virtualRel.setWeight(0);
            save(virtualRel);
            createdCount++;
        }
        return createdCount;
    }

    @Override
    public void updateRelationship(Long id, Relationship relationship) {
        Relationship existing = getById(id);
        if (existing == null) {
            throw new IllegalArgumentException("关系不存在");
        }
        existing.setRelationTypes(relationship.getRelationTypes());
        existing.setRemark(relationship.getRemark());
        existing.setWeight(relationship.getWeight());
        updateById(existing);
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

    private String buildPairKey(Long fromId, Long toId) {
        return fromId + "-" + toId;
    }
}
