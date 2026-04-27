package com.prd.service;

import com.prd.dto.ExportDataDTO;
import com.prd.dto.ImportPreviewDTO;
import com.prd.dto.ImportRequestDTO;

public interface ExportImportService {

    ExportDataDTO exportAll();

    ImportPreviewDTO previewImport(ExportDataDTO data);

    void importData(ImportRequestDTO request);
}
