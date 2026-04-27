package com.prd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.prd.entity.Relationship;

import java.util.List;

public interface RelationshipService extends IService<Relationship> {

    List<Relationship> findByPersonId(Long personId);
}
