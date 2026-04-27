package com.prd.dto;

import lombok.Data;

import java.util.List;

@Data
public class BatchGroupsDTO {
    private List<Long> personIds;
    private Long groupsId;
}
