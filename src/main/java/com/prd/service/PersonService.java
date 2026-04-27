package com.prd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.prd.dto.BatchGroupsDTO;
import com.prd.dto.BatchPositionDTO;
import com.prd.entity.Person;

import java.util.List;

public interface PersonService extends IService<Person> {

    Long createPerson(Person person);

    void updatePerson(Long id, Person person);

    void deletePerson(Long id);

    List<Person> searchByKeyword(String keyword);

    void batchUpdatePositions(BatchPositionDTO dto);

    void batchMoveToGroup(BatchGroupsDTO dto);
}
