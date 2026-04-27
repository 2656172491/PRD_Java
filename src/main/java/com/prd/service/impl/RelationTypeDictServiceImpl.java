package com.prd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.prd.entity.RelationTypeDict;
import com.prd.mapper.RelationTypeDictMapper;
import com.prd.service.RelationTypeDictService;
import org.springframework.stereotype.Service;

@Service
public class RelationTypeDictServiceImpl extends ServiceImpl<RelationTypeDictMapper, RelationTypeDict> implements RelationTypeDictService {
}
