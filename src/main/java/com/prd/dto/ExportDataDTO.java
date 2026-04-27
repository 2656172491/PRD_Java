package com.prd.dto;

import com.prd.entity.Group;
import com.prd.entity.Person;
import com.prd.entity.Relationship;
import lombok.Data;

import java.util.List;

@Data
public class ExportDataDTO {
    private String version = "1.2";
    private List<Person> persons;
    private List<Relationship> relationships;
    private List<Group> groups;
    private List<String> relationTypeDict;
}
