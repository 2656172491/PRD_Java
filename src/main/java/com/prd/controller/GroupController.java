package com.prd.controller;

import com.prd.common.Result;
import com.prd.entity.Group;
import com.prd.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public Result<List<Group>> list() {
        return Result.success(groupService.list());
    }

    @PostMapping
    public Result<Long> save(@RequestBody Group group) {
        groupService.save(group);
        return Result.success(group.getId());
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Group group) {
        group.setId(id);
        groupService.updateById(group);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return Result.success();
    }
}
