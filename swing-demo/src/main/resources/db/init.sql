-- 创建数据库
CREATE DATABASE IF NOT EXISTS personality_analysis DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 使用数据库
USE user_db;

-- 创建用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    create_time VARCHAR(20) COMMENT '创建时间',
    update_time VARCHAR(20) COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入测试数据
INSERT INTO user (username, password, email, create_time, update_time) VALUES
('admin', 'admin123', 'admin@example.com', '2024-03-20 10:00:00', '2024-03-20 10:00:00'),
('test', 'test123', 'test@example.com', '2024-03-20 10:00:00', '2024-03-20 10:00:00');

-- 创建索引
CREATE INDEX idx_username ON user(username);
CREATE INDEX idx_email ON user(email); 