/*
 Navicat Premium Dump SQL

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : 127.0.0.1:3306
 Source Schema         : ordercode

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 15/06/2025 19:18:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for books
-- ----------------------------
DROP TABLE IF EXISTS `books`;
CREATE TABLE `books`  (
  `book_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '图书ID，主键',
  `isbn` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ISBN号',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '书名',
  `subtitle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '副标题',
  `original_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '原书名',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '作者',
  `author_bio` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '作者简介',
  `translator` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '译者',
  `publisher` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '出版社',
  `publish_date` date NOT NULL COMMENT '出版日期',
  `publish_edition` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '版次',
  `publish_place` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '出版地',
  `category_id` int UNSIGNED NULL DEFAULT NULL COMMENT '分类ID',
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类名称',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签，多个标签用逗号分隔',
  `language` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '中文' COMMENT '语言',
  `format` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '装帧形式（平装/精装）',
  `page_count` int UNSIGNED NULL DEFAULT NULL COMMENT '页数',
  `word_count` int UNSIGNED NULL DEFAULT NULL COMMENT '字数',
  `size` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开本',
  `weight` decimal(5, 2) NULL DEFAULT NULL COMMENT '重量(克)',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容简介',
  `catalog` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '目录',
  `preface` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '前言',
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '引言',
  `price` decimal(10, 2) NOT NULL COMMENT '定价',
  `discount_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '折扣价',
  `market_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '市场价',
  `stock_quantity` int NOT NULL DEFAULT 0 COMMENT '库存数量',
  `sales_volume` int NOT NULL DEFAULT 0 COMMENT '销量',
  `rating` decimal(2, 1) NULL DEFAULT NULL COMMENT '评分(0-5)',
  `rating_count` int UNSIGNED NULL DEFAULT 0 COMMENT '评分人数',
  `has_ebook` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否有电子版',
  `ebook_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '电子书价格',
  `ebook_format` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电子书格式',
  `cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `sample_chapter` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '试读章节',
  `series_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '丛书名',
  `series_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '丛书序号',
  `awards` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '获奖情况',
  `recommendation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '推荐语',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态(1:在售,2:下架,3:缺货)',
  `is_recommended` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否推荐',
  `is_bestseller` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否畅销书',
  `is_new` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否新书',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `last_restock_time` timestamp NULL DEFAULT NULL COMMENT '最后补货时间',
  PRIMARY KEY (`book_id`) USING BTREE,
  UNIQUE INDEX `uk_isbn`(`isbn` ASC) USING BTREE,
  INDEX `idx_title`(`title` ASC) USING BTREE,
  INDEX `idx_author`(`author` ASC) USING BTREE,
  INDEX `idx_category`(`category_id` ASC) USING BTREE,
  INDEX `idx_publisher`(`publisher` ASC) USING BTREE,
  INDEX `idx_publish_date`(`publish_date` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_rating`(`rating` ASC) USING BTREE,
  INDEX `idx_sales`(`sales_volume` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1934205347302637572 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '图书信息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
