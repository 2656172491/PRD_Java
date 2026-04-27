package com.prd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@TableName(value = "person", autoResultMap = true)
public class Person {
    @TableId(type = IdType.AUTO)
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

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> customFields;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
