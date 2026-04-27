package com.prd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.prd.dto.BatchPositionDTO;
import com.prd.entity.Person;
import com.prd.mapper.PersonMapper;
import com.prd.service.PersonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements PersonService {

    @Override
    public List<Person> searchByKeyword(String keyword) {
        LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Person::getName, keyword);
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
}
