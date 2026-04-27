package com.prd.dto;

import lombok.Data;

@Data
public class ImportPreviewDTO {
    private int newCount;
    private int updateCount;
    private int invalidCount;
}
