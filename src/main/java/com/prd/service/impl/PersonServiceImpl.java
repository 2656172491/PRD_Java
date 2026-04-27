package com.prd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.prd.dto.BatchGroupsDTO;
import com.prd.dto.BatchPositionDTO;
import com.prd.entity.Person;
import com.prd.mapper.PersonMapper;
import com.prd.mapper.RelationshipMapper;
import com.prd.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements PersonService {

    private final RelationshipMapper relationshipMapper;

    @Override
    public Long createPerson(Person person) {
        validateNameForCreate(person);
        save(person);
        return person.getId();
    }

    @Override
    @Transactional
    public void updatePerson(Long id, Person person) {
        Person existing = getById(id);
        if (existing == null) {
            throw new IllegalArgumentException("联系人不存在: " + id);
        }

        applyNameForUpdate(existing, person);
        if (person.getGroupId() != null) {
            existing.setGroupId(person.getGroupId());
        }
        if (person.getPositionX() != null) {
            existing.setPositionX(person.getPositionX());
        }
        if (person.getPositionY() != null) {
            existing.setPositionY(person.getPositionY());
        }
        if (person.getData() != null) {
            existing.setData(person.getData());
        }

        updateById(existing);
    }

    @Override
    @Transactional
    public void deletePerson(Long id) {
        LambdaQueryWrapper<com.prd.entity.Relationship> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(com.prd.entity.Relationship::getFromPersonId, id)
                .or()
                .eq(com.prd.entity.Relationship::getToPersonId, id);
        relationshipMapper.delete(wrapper);
        removeById(id);
    }

    @Override
    public List<Person> searchByKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return list();
        }
        LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Person::getName, keyword.trim());
        return list(wrapper);
    }

    @Override
    @Transactional
    public void batchUpdatePositions(BatchPositionDTO dto) {
        if (dto.getPositions() == null || dto.getPositions().isEmpty()) {
            return;
        }
        for (BatchPositionDTO.PositionItem item : dto.getPositions()) {
            Person person = new Person();
            person.setId(item.getId());
            person.setPositionX(item.getPositionX());
            person.setPositionY(item.getPositionY());
            updateById(person);
        }
    }

    @Override
    @Transactional
    public void batchMoveToGroup(BatchGroupsDTO dto) {
        if (dto.getPersonIds() == null || dto.getPersonIds().isEmpty()) {
            return;
        }
        LambdaUpdateWrapper<Person> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Person::getId, dto.getPersonIds())
                .set(Person::getGroupId, dto.getGroupsId());
        update(wrapper);
    }

    private void validateNameForCreate(Person person) {
        if (person == null || !StringUtils.hasText(person.getName())) {
            throw new IllegalArgumentException("姓名不能为空");
        }
        person.setName(person.getName().trim());
    }

    private void applyNameForUpdate(Person existing, Person incoming) {
        if (incoming == null) {
            throw new IllegalArgumentException("联系人更新参数不能为空");
        }
        if (!StringUtils.hasText(incoming.getName())) {
            return;
        }
        existing.setName(incoming.getName().trim());
    }
}
