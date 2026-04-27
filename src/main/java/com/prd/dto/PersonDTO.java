package com.prd.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class PersonDTO {
    private Long id;
    private String name;
    private String avatarUrl;
    private String phone;
    private String email;
    private String address;
    private String hobby;
    private LocalDate firstMetDate;
    private String firstMetPlace;
    private String remark;
    private Long groupId;
    private Double positionX;
    private Double positionY;
    private Map<String, Object> customFields;
}
