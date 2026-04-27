package com.prd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.prd.entity.RelationTypeDict;

import java.util.List;

public interface RelationTypeDictService extends IService<RelationTypeDict> {

    List<RelationTypeDict> listAll();

    Integer createRelationType(RelationTypeDict dict);
}
