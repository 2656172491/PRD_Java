package com.prd.controller;

import com.prd.common.Result;
import com.prd.entity.Relationship;
import com.prd.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/relationships")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RelationshipController {

    private final RelationshipService relationshipService;

    @GetMapping
    public Result<List<Relationship>> list() {
        return Result.success(relationshipService.list());
    }

    @PostMapping
    public Result<Long> save(@RequestBody Relationship relationship) {
        return Result.success(relationshipService.createRelationship(relationship));
    }

    @PostMapping("/virtual/{selfPersonId}")
    public Result<Integer> rebuildVirtual(@PathVariable Long selfPersonId) {
        return Result.success(relationshipService.rebuildVirtualRelationships(selfPersonId));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Relationship relationship) {
        relationshipService.updateRelationship(id, relationship);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        relationshipService.deleteRelationship(id);
        return Result.success();
    }
}
