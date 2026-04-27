package com.prd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.prd.entity.Relationship;

import java.util.List;

public interface RelationshipService extends IService<Relationship> {

    Long createRelationship(Relationship relationship);

    Integer rebuildVirtualRelationships(Long selfPersonId);

    void deleteRelationship(Long id);

    List<Relationship> findByPersonId(Long personId);
}
