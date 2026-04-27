package com.prd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "relationship", autoResultMap = true)
public class Relationship {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long fromPersonId;
    private Long toPersonId;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> relationTypes;

    private String remark;
    private Integer weight;
    private LocalDateTime createTime;
}
