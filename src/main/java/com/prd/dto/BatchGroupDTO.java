package com.prd.dto;

import lombok.Data;

import java.util.List;

@Data
public class BatchGroupDTO {
    private List<Long> personIds;
    private Long groupId;
}
