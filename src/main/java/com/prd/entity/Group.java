package com.prd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("group")
public class Group {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String color;
    private Boolean collapsed;
    private LocalDateTime createTime;
}
