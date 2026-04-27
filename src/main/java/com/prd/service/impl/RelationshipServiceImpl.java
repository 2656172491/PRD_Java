package com.prd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.prd.entity.Relationship;
import com.prd.mapper.RelationshipMapper;
import com.prd.service.RelationshipService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationshipServiceImpl extends ServiceImpl<RelationshipMapper, Relationship> implements RelationshipService {

    @Override
    public List<Relationship> findByPersonId(Long personId) {
        return baseMapper.findByPersonId(personId);
    }
}
