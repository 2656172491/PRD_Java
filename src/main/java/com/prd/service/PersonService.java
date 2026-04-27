package com.prd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.prd.dto.BatchPositionDTO;
import com.prd.entity.Person;

import java.util.List;

public interface PersonService extends IService<Person> {

    List<Person> searchByKeyword(String keyword);

    void batchUpdatePositions(BatchPositionDTO dto);
}
