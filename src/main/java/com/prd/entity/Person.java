package com.prd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@TableName(value = "person", autoResultMap = true)
public class Person {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Long groupId;
    private Double positionX;
    private Double positionY;

    /**
     * 联系人的所有扩展信息，以 key-value 形式存储。
     * 例如：{"phone": "13800138000", "email": "a@b.com", "生日": "1990-01-01"}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> data;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
