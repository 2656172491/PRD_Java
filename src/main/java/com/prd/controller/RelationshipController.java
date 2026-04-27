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
        // 规范化：from_person_id < to_person_id
        if (relationship.getFromPersonId() > relationship.getToPersonId()) {
            Long temp = relationship.getFromPersonId();
            relationship.setFromPersonId(relationship.getToPersonId());
            relationship.setToPersonId(temp);
        }
        relationshipService.save(relationship);
        return Result.success(relationship.getId());
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        relationshipService.removeById(id);
        return Result.success();
    }
}
