package com.prd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.prd.entity.Group;

public interface GroupService extends IService<Group> {

    boolean deleteGroup(Long id);
}
