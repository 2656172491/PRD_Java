package com.prd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("relation_type_dict")
public class RelationTypeDict {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String typeName;
    private String color;
}
