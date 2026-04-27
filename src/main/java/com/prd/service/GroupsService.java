package com.prd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.prd.dto.BatchPositionDTO;
import com.prd.entity.Groups;

public interface GroupsService extends IService<Groups> {

    Long createGroup(Groups groups);

    void updateGroup(Long id, Groups groups);

    boolean deleteGroup(Long id);

    void batchUpdatePositions(BatchPositionDTO dto);
}
