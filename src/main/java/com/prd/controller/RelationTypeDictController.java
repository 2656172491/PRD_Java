package com.prd.controller;

import com.prd.common.Result;
import com.prd.entity.RelationTypeDict;
import com.prd.service.RelationTypeDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/relation-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RelationTypeDictController {

    private final RelationTypeDictService relationTypeDictService;

    @GetMapping
    public Result<List<RelationTypeDict>> list() {
        return Result.success(relationTypeDictService.listAll());
    }

    @PostMapping
    public Result<Integer> save(@RequestBody RelationTypeDict dict) {
        return Result.success(relationTypeDictService.createRelationType(dict));
    }
}
