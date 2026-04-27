package com.prd.dto;

import lombok.Data;

@Data
public class ImportRequestDTO {
    private String mode; // 'replace' 或 'merge'
    private ExportDataDTO data;
}
