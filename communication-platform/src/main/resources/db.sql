-- 创建数据库
CREATE DATABASE IF NOT EXISTS communication_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE communication_platform;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) UNIQUE,
    `nickname` VARCHAR(50),
    `avatar_url` VARCHAR(255),
    `bio` TEXT,
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '用户状态：0-禁用，1-启用',
    `last_login_time` DATETIME,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 问题表
CREATE TABLE IF NOT EXISTS `question` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `title` VARCHAR(200) NOT NULL,
    `description` TEXT NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `view_count` INT NOT NULL DEFAULT 0,
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time),
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 回答表
CREATE TABLE IF NOT EXISTS `answer` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `question_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `content` TEXT NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_question_id (question_id),
    INDEX idx_user_id (user_id),
    FOREIGN KEY (question_id) REFERENCES question(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 