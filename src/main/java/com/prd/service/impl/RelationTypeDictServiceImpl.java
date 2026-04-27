package com.prd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.prd.entity.RelationTypeDict;
import com.prd.mapper.RelationTypeDictMapper;
import com.prd.service.RelationTypeDictService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class RelationTypeDictServiceImpl extends ServiceImpl<RelationTypeDictMapper, RelationTypeDict> implements RelationTypeDictService {

    @Override
    public List<RelationTypeDict> listAll() {
        return list();
    }

    @Override
    public Integer createRelationType(RelationTypeDict dict) {
        if (dict == null || !StringUtils.hasText(dict.getTypeName())) {
            throw new IllegalArgumentException("关系类型不能为空");
        }
        String typeName = dict.getTypeName().trim();

        LambdaQueryWrapper<RelationTypeDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RelationTypeDict::getTypeName, typeName);
        RelationTypeDict existing = getOne(wrapper, false);
        if (existing != null) {
            return existing.getId();
        }

        dict.setTypeName(typeName);
        save(dict);
        return dict.getId();
    }
}
