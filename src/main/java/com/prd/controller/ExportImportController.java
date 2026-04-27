package com.prd.controller;

import com.prd.common.Result;
import com.prd.dto.ExportDataDTO;
import com.prd.dto.ImportPreviewDTO;
import com.prd.dto.ImportRequestDTO;
import com.prd.service.ExportImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ExportImportController {

    private final ExportImportService exportImportService;

    @GetMapping("/export/json")
    public Result<ExportDataDTO> exportJson() {
        return Result.success(exportImportService.exportAll());
    }

    @PostMapping("/import/json/preview")
    public Result<ImportPreviewDTO> previewImport(@RequestBody ExportDataDTO data) {
        return Result.success(exportImportService.previewImport(data));
    }

    @PostMapping("/import/json")
    public Result<Void> importJson(@RequestBody ImportRequestDTO request) {
        exportImportService.importData(request);
        return Result.success();
    }
}
