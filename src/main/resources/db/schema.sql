-- 联系人表：核心字段固定列 + 其他信息全部走 data JSON
CREATE TABLE IF NOT EXISTS person (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    group_id BIGINT,
    position_x DOUBLE,
    position_y DOUBLE,
    data JSON,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FULLTEXT INDEX idx_name (name)
);

-- 关系边表
CREATE TABLE IF NOT EXISTS relationship (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    from_person_id BIGINT NOT NULL,
    to_person_id BIGINT NOT NULL,
    relation_types JSON NOT NULL,
    remark VARCHAR(500),
    weight INT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 分组表
CREATE TABLE IF NOT EXISTS `group` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    color VARCHAR(20),
    collapsed TINYINT(1) DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 关系类型字典表
CREATE TABLE IF NOT EXISTS relation_type_dict (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) UNIQUE NOT NULL
);

-- 插入默认关系类型
INSERT IGNORE INTO relation_type_dict (type_name) VALUES ('朋友');
INSERT IGNORE INTO relation_type_dict (type_name) VALUES ('同事');
INSERT IGNORE INTO relation_type_dict (type_name) VALUES ('家人');
INSERT IGNORE INTO relation_type_dict (type_name) VALUES ('同学');
INSERT IGNORE INTO relation_type_dict (type_name) VALUES ('合作伙伴');
