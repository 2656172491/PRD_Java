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
    is_virtual TINYINT(1) NOT NULL DEFAULT 0,
    remark VARCHAR(500),
    weight INT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

SET @has_is_virtual := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'relationship'
      AND COLUMN_NAME = 'is_virtual'
);
SET @ddl_is_virtual := IF(
    @has_is_virtual = 0,
    'ALTER TABLE relationship ADD COLUMN is_virtual TINYINT(1) NOT NULL DEFAULT 0',
    'SELECT 1'
);
PREPARE stmt_is_virtual FROM @ddl_is_virtual;
EXECUTE stmt_is_virtual;
DEALLOCATE PREPARE stmt_is_virtual;

-- 分组表
CREATE TABLE IF NOT EXISTS `groups` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    color VARCHAR(20),
    collapsed TINYINT(1) DEFAULT 0,
    parent_id BIGINT,
    position_x DOUBLE,
    position_y DOUBLE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 关系类型字典表
CREATE TABLE IF NOT EXISTS relation_type_dict (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) UNIQUE NOT NULL,
    color VARCHAR(20)
);

-- 插入默认关系类型（带颜色）
INSERT IGNORE INTO relation_type_dict (type_name, color) VALUES ('默认', '#94a3b8');
INSERT IGNORE INTO relation_type_dict (type_name, color) VALUES ('朋友', '#38bdf8');
INSERT IGNORE INTO relation_type_dict (type_name, color) VALUES ('同事', '#818cf8');
INSERT IGNORE INTO relation_type_dict (type_name, color) VALUES ('家人', '#f472b6');
INSERT IGNORE INTO relation_type_dict (type_name, color) VALUES ('同学', '#34d399');
INSERT IGNORE INTO relation_type_dict (type_name, color) VALUES ('合作伙伴', '#fbbf24');

-- 插入默认分组"未分组"
INSERT IGNORE INTO `groups` (name, color, collapsed) VALUES ('未分组', '#64748b', 0);

-- 为已存在的 groups 表添加 parent_id 列（兼容升级）
SET @has_parent_id := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'groups'
      AND COLUMN_NAME = 'parent_id'
);
SET @ddl_parent_id := IF(
    @has_parent_id = 0,
    'ALTER TABLE `groups` ADD COLUMN parent_id BIGINT',
    'SELECT 1'
);
PREPARE stmt_parent_id FROM @ddl_parent_id;
EXECUTE stmt_parent_id;
DEALLOCATE PREPARE stmt_parent_id;

-- 为已存在的 groups 表添加 position_x/position_y 列（兼容升级）
SET @has_pos_x := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'groups'
      AND COLUMN_NAME = 'position_x'
);
SET @ddl_pos_x := IF(
    @has_pos_x = 0,
    'ALTER TABLE `groups` ADD COLUMN position_x DOUBLE',
    'SELECT 1'
);
PREPARE stmt_pos_x FROM @ddl_pos_x;
EXECUTE stmt_pos_x;
DEALLOCATE PREPARE stmt_pos_x;

SET @has_pos_y := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'groups'
      AND COLUMN_NAME = 'position_y'
);
SET @ddl_pos_y := IF(
    @has_pos_y = 0,
    'ALTER TABLE `groups` ADD COLUMN position_y DOUBLE',
    'SELECT 1'
);
PREPARE stmt_pos_y FROM @ddl_pos_y;
EXECUTE stmt_pos_y;
DEALLOCATE PREPARE stmt_pos_y;

-- 为已存在的 relation_type_dict 表添加 color 列（兼容升级）
SET @has_color := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'relation_type_dict'
      AND COLUMN_NAME = 'color'
);
SET @ddl_color := IF(
    @has_color = 0,
    'ALTER TABLE relation_type_dict ADD COLUMN color VARCHAR(20)',
    'SELECT 1'
);
PREPARE stmt_color FROM @ddl_color;
EXECUTE stmt_color;
DEALLOCATE PREPARE stmt_color;

-- 插入默认关系类型（如果不存在）
INSERT IGNORE INTO relation_type_dict (type_name, color) VALUES ('默认', '#94a3b8');
