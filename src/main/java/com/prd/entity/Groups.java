package com.prd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("`groups`")
public class Groups {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String color;
    private Boolean collapsed;
    private Long parentId;
    private Double positionX;
    private Double positionY;
    private LocalDateTime createTime;
}
