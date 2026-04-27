package com.prd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.prd.dto.ExportDataDTO;
import com.prd.dto.ImportPreviewDTO;
import com.prd.dto.ImportRequestDTO;
import com.prd.entity.Group;
import com.prd.entity.Person;
import com.prd.entity.RelationTypeDict;
import com.prd.entity.Relationship;
import com.prd.mapper.GroupMapper;
import com.prd.mapper.PersonMapper;
import com.prd.mapper.RelationTypeDictMapper;
import com.prd.mapper.RelationshipMapper;
import com.prd.service.ExportImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExportImportServiceImpl implements ExportImportService {

    private final PersonMapper personMapper;
    private final RelationshipMapper relationshipMapper;
    private final GroupMapper groupMapper;
    private final RelationTypeDictMapper relationTypeDictMapper;

    @Override
    public ExportDataDTO exportAll() {
        ExportDataDTO dto = new ExportDataDTO();
        dto.setPersons(personMapper.selectList(null));
        dto.setRelationships(relationshipMapper.selectList(null));
        dto.setGroups(groupMapper.selectList(null));
        List<RelationTypeDict> dictList = relationTypeDictMapper.selectList(null);
        dto.setRelationTypeDict(dictList.stream().map(RelationTypeDict::getTypeName).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public ImportPreviewDTO previewImport(ExportDataDTO data) {
        ImportPreviewDTO preview = new ImportPreviewDTO();
        if (data.getPersons() == null) {
            preview.setInvalidCount(0);
            return preview;
        }

        int newCount = 0;
        int updateCount = 0;
        int invalidCount = 0;

        for (Person p : data.getPersons()) {
            if (p.getName() == null || p.getName().trim().isEmpty()) {
                invalidCount++;
                continue;
            }
            Person existing = findByNameAndPhone(p.getName(), p.getPhone());
            if (existing == null) {
                newCount++;
            } else {
                updateCount++;
            }
        }

        preview.setNewCount(newCount);
        preview.setUpdateCount(updateCount);
        preview.setInvalidCount(invalidCount);
        return preview;
    }

    @Override
    @Transactional
    public void importData(ImportRequestDTO request) {
        ExportDataDTO data = request.getData();
        String mode = request.getMode();

        if ("replace".equals(mode)) {
            // 覆盖模式：清空所有数据
            relationshipMapper.delete(null);
            personMapper.delete(null);
            groupMapper.delete(null);
            relationTypeDictMapper.delete(null);
        }

        // 导入分组
        Map<Long, Long> groupIdMap = new HashMap<>();
        if (data.getGroups() != null) {
            for (Group g : data.getGroups()) {
                Long oldId = g.getId();
                g.setId(null);
                groupMapper.insert(g);
                if (oldId != null) {
                    groupIdMap.put(oldId, g.getId());
                }
            }
        }

        // 导入关系类型字典
        if (data.getRelationTypeDict() != null) {
            for (String typeName : data.getRelationTypeDict()) {
                LambdaQueryWrapper<RelationTypeDict> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(RelationTypeDict::getTypeName, typeName);
                if (relationTypeDictMapper.selectCount(wrapper) == 0) {
                    RelationTypeDict dict = new RelationTypeDict();
                    dict.setTypeName(typeName);
                    relationTypeDictMapper.insert(dict);
                }
            }
        }

        // 导入联系人
        Map<Long, Long> personIdMap = new HashMap<>();
        if (data.getPersons() != null) {
            for (Person p : data.getPersons()) {
                if (p.getName() == null || p.getName().trim().isEmpty()) {
                    continue;
                }
                Long oldId = p.getId();
                Person existing = findByNameAndPhone(p.getName(), p.getPhone());

                if ("merge".equals(mode) && existing != null) {
                    // 更新现有记录
                    p.setId(existing.getId());
                    if (p.getGroupId() != null && groupIdMap.containsKey(p.getGroupId())) {
                        p.setGroupId(groupIdMap.get(p.getGroupId()));
                    }
                    personMapper.updateById(p);
                    personIdMap.put(oldId, existing.getId());
                } else {
                    // 新增
                    p.setId(null);
                    if (p.getGroupId() != null && groupIdMap.containsKey(p.getGroupId())) {
                        p.setGroupId(groupIdMap.get(p.getGroupId()));
                    }
                    personMapper.insert(p);
                    if (oldId != null) {
                        personIdMap.put(oldId, p.getId());
                    }
                }
            }
        }

        // 导入关系边
        if (data.getRelationships() != null) {
            for (Relationship r : data.getRelationships()) {
                Long fromId = personIdMap.getOrDefault(r.getFromPersonId(), r.getFromPersonId());
                Long toId = personIdMap.getOrDefault(r.getToPersonId(), r.getToPersonId());

                // 规范化
                if (fromId > toId) {
                    Long temp = fromId;
                    fromId = toId;
                    toId = temp;
                }

                // 检查是否已存在相同关系
                LambdaQueryWrapper<Relationship> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Relationship::getFromPersonId, fromId)
                        .eq(Relationship::getToPersonId, toId);
                List<Relationship> existingList = relationshipMapper.selectList(wrapper);

                boolean exists = false;
                for (Relationship existing : existingList) {
                    if (existing.getRelationTypes() != null && r.getRelationTypes() != null
                            && new HashSet<>(existing.getRelationTypes()).containsAll(r.getRelationTypes())) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    r.setId(null);
                    r.setFromPersonId(fromId);
                    r.setToPersonId(toId);
                    relationshipMapper.insert(r);
                }
            }
        }
    }

    private Person findByNameAndPhone(String name, String phone) {
        LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Person::getName, name);
        if (phone != null && !phone.isEmpty()) {
            wrapper.eq(Person::getPhone, phone);
        }
        List<Person> list = personMapper.selectList(wrapper);
        return list.isEmpty() ? null : list.get(0);
    }
}
