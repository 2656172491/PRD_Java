package com.prd.controller;

import com.prd.common.Result;
import com.prd.entity.Groups;
import com.prd.service.GroupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class GroupsController {

    private final GroupsService groupsService;

    @GetMapping
    public Result<List<Groups>> list() {
        return Result.success(groupsService.list());
    }

    @PostMapping
    public Result<Long> save(@RequestBody Groups groups) {
        return Result.success(groupsService.createGroup(groups));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Groups groups) {
        groupsService.updateGroup(id, groups);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        groupsService.deleteGroup(id);
        return Result.success();
    }
}
