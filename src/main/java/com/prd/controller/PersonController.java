package com.prd.controller;

import com.prd.common.Result;
import com.prd.dto.BatchGroupsDTO;
import com.prd.dto.BatchPositionDTO;
import com.prd.entity.Person;
import com.prd.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public Result<List<Person>> list() {
        return Result.success(personService.list());
    }

    @GetMapping("/search")
    public Result<List<Person>> search(@RequestParam String keyword) {
        return Result.success(personService.searchByKeyword(keyword));
    }

    @GetMapping("/{id}")
    public Result<Person> getById(@PathVariable Long id) {
        return Result.success(personService.getById(id));
    }

    @PostMapping
    public Result<Long> save(@RequestBody Person person) {
        return Result.success(personService.createPerson(person));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Person person) {
        personService.updatePerson(id, person);
        return Result.success();
    }

    @PutMapping("/batch/positions")
    public Result<Void> batchUpdatePositions(@RequestBody BatchPositionDTO dto) {
        personService.batchUpdatePositions(dto);
        return Result.success();
    }

    @PutMapping("/batch/group")
    public Result<Void> batchMoveToGroup(@RequestBody BatchGroupsDTO dto) {
        personService.batchMoveToGroup(dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        personService.deletePerson(id);
        return Result.success();
    }
}
