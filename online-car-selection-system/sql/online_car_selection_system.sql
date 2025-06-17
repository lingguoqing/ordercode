/*
 Navicat Premium Data Transfer

 Source Server         : localhost_mysql
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : online_car_selection_system

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 17/06/2025 15:35:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for brand
-- ----------------------------
DROP TABLE IF EXISTS `brand`;
CREATE TABLE `brand`  (
  `brand_id` int NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
  `brand_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品牌名称',
  PRIMARY KEY (`brand_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '品牌表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of brand
-- ----------------------------
INSERT INTO `brand` VALUES (1, '丰田');
INSERT INTO `brand` VALUES (2, '大众');
INSERT INTO `brand` VALUES (3, '本田');
INSERT INTO `brand` VALUES (4, '奔驰');
INSERT INTO `brand` VALUES (5, '宝马');
INSERT INTO `brand` VALUES (6, '奥迪');
INSERT INTO `brand` VALUES (7, '特斯拉');
INSERT INTO `brand` VALUES (8, '日产');
INSERT INTO `brand` VALUES (9, '福特');
INSERT INTO `brand` VALUES (10, '现代');

-- ----------------------------
-- Table structure for car
-- ----------------------------
DROP TABLE IF EXISTS `car`;
CREATE TABLE `car`  (
  `car_id` int NOT NULL AUTO_INCREMENT COMMENT '车辆ID',
  `production_year` int NULL DEFAULT NULL COMMENT '生产年份',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '颜色',
  `mileage` int NULL DEFAULT NULL COMMENT '里程数',
  `inventory_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '库存状态',
  `model_id` int NOT NULL COMMENT '车型ID (外键)',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车辆图片URL',
  PRIMARY KEY (`car_id`) USING BTREE,
  INDEX `model_id`(`model_id` ASC) USING BTREE,
  CONSTRAINT `car_ibfk_1` FOREIGN KEY (`model_id`) REFERENCES `car_model` (`model_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '车辆表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of car
-- ----------------------------
TRUNCATE TABLE `car`; -- 清空现有数据以便重新插入
INSERT INTO `car` VALUES (1, 2022, 100000.00, '白色', 500, '有货', 1, 'https://example.com/car_images/car1.jpg');
INSERT INTO `car` VALUES (2, 2023, 103100.00, '黑色', 700, '有货', 2, 'https://example.com/car_images/car2.jpg');
INSERT INTO `car` VALUES (3, 2021, 106200.00, '银色', 900, '有货', 3, 'https://example.com/car_images/car3.jpg');
INSERT INTO `car` VALUES (4, 2022, 109300.00, '灰色', 1100, '有货', 4, 'https://example.com/car_images/car4.jpg');
INSERT INTO `car` VALUES (5, 2023, 112400.00, '蓝色', 1300, '有货', 5, 'https://example.com/car_images/car5.jpg');
INSERT INTO `car` VALUES (6, 2021, 115500.00, '红色', 1500, '有货', 6, 'https://example.com/car_images/car6.jpg');
INSERT INTO `car` VALUES (7, 2022, 118600.00, '金色', 1700, '有货', 7, 'https://example.com/car_images/car7.jpg');
INSERT INTO `car` VALUES (8, 2023, 121700.00, '棕色', 1900, '有货', 8, 'https://example.com/car_images/car8.jpg');
INSERT INTO `car` VALUES (9, 2021, 124800.00, '绿色', 2100, '有货', 9, 'https://example.com/car_images/car9.jpg');
INSERT INTO `car` VALUES (10, 2022, 127900.00, '珍珠白', 2300, '有货', 10, 'https://example.com/car_images/car10.jpg');
INSERT INTO `car` VALUES (11, 2023, 131000.00, '白色', 2500, '有货', 11, 'https://example.com/car_images/car11.jpg');
INSERT INTO `car` VALUES (12, 2021, 134100.00, '黑色', 2700, '有货', 12, 'https://example.com/car_images/car12.jpg');
INSERT INTO `car` VALUES (13, 2022, 137200.00, '银色', 2900, '有货', 13, 'https://example.com/car_images/car13.jpg');
INSERT INTO `car` VALUES (14, 2023, 140300.00, '灰色', 3100, '有货', 14, 'https://example.com/car_images/car14.jpg');
INSERT INTO `car` VALUES (15, 2021, 143400.00, '蓝色', 3300, '有货', 15, 'https://example.com/car_images/car15.jpg');
INSERT INTO `car` VALUES (16, 2022, 146500.00, '红色', 3500, '有货', 16, 'https://example.com/car_images/car16.jpg');
INSERT INTO `car` VALUES (17, 2023, 149600.00, '金色', 3700, '有货', 17, 'https://example.com/car_images/car17.jpg');
INSERT INTO `car` VALUES (18, 2021, 152700.00, '棕色', 3900, '有货', 18, 'https://example.com/car_images/car18.jpg');
INSERT INTO `car` VALUES (19, 2022, 155800.00, '绿色', 4100, '有货', 19, 'https://example.com/car_images/car19.jpg');
INSERT INTO `car` VALUES (20, 2023, 158900.00, '珍珠白', 4300, '有货', 20, 'https://example.com/car_images/car20.jpg');
INSERT INTO `car` VALUES (21, 2021, 162000.00, '白色', 4500, '有货', 1, 'https://example.com/car_images/car21.jpg');
INSERT INTO `car` VALUES (22, 2022, 165100.00, '黑色', 4700, '有货', 2, 'https://example.com/car_images/car22.jpg');
INSERT INTO `car` VALUES (23, 2023, 168200.00, '银色', 4900, '有货', 3, 'https://example.com/car_images/car23.jpg');
INSERT INTO `car` VALUES (24, 2021, 171300.00, '灰色', 5100, '有货', 4, 'https://example.com/car_images/car24.jpg');
INSERT INTO `car` VALUES (25, 2022, 174400.00, '蓝色', 5300, '有货', 5, 'https://example.com/car_images/car25.jpg');
INSERT INTO `car` VALUES (26, 2023, 177500.00, '红色', 5500, '有货', 6, 'https://example.com/car_images/car26.jpg');
INSERT INTO `car` VALUES (27, 2021, 180600.00, '金色', 5700, '有货', 7, 'https://example.com/car_images/car27.jpg');
INSERT INTO `car` VALUES (28, 2022, 183700.00, '棕色', 5900, '有货', 8, 'https://example.com/car_images/car28.jpg');
INSERT INTO `car` VALUES (29, 2023, 186800.00, '绿色', 6100, '有货', 9, 'https://example.com/car_images/car29.jpg');
INSERT INTO `car` VALUES (30, 2021, 189900.00, '珍珠白', 6300, '有货', 10, 'https://example.com/car_images/car30.jpg');
INSERT INTO `car` VALUES (31, 2022, 193000.00, '白色', 6500, '有货', 11, 'https://example.com/car_images/car31.jpg');
INSERT INTO `car` VALUES (32, 2023, 196100.00, '黑色', 6700, '有货', 12, 'https://example.com/car_images/car32.jpg');
INSERT INTO `car` VALUES (33, 2021, 199200.00, '银色', 6900, '有货', 13, 'https://example.com/car_images/car33.jpg');
INSERT INTO `car` VALUES (34, 2022, 202300.00, '灰色', 7100, '有货', 14, 'https://example.com/car_images/car34.jpg');
INSERT INTO `car` VALUES (35, 2023, 205400.00, '蓝色', 7300, '有货', 15, 'https://example.com/car_images/car35.jpg');
INSERT INTO `car` VALUES (36, 2021, 208500.00, '红色', 7500, '有货', 16, 'https://example.com/car_images/car36.jpg');
INSERT INTO `car` VALUES (37, 2022, 211600.00, '金色', 7700, '有货', 17, 'https://example.com/car_images/car37.jpg');
INSERT INTO `car` VALUES (38, 2023, 214700.00, '棕色', 7900, '有货', 18, 'https://example.com/car_images/car38.jpg');
INSERT INTO `car` VALUES (39, 2021, 217800.00, '绿色', 8100, '有货', 19, 'https://example.com/car_images/car39.jpg');
INSERT INTO `car` VALUES (40, 2022, 220900.00, '珍珠白', 8300, '有货', 20, 'https://example.com/car_images/car40.jpg');
INSERT INTO `car` VALUES (41, 2023, 224000.00, '白色', 8500, '有货', 1, 'https://example.com/car_images/car41.jpg');
INSERT INTO `car` VALUES (42, 2021, 227100.00, '黑色', 8700, '有货', 2, 'https://example.com/car_images/car42.jpg');
INSERT INTO `car` VALUES (43, 2022, 230200.00, '银色', 8900, '有货', 3, 'https://example.com/car_images/car43.jpg');
INSERT INTO `car` VALUES (44, 2023, 233300.00, '灰色', 9100, '有货', 4, 'https://example.com/car_images/car44.jpg');
INSERT INTO `car` VALUES (45, 2021, 236400.00, '蓝色', 9300, '有货', 5, 'https://example.com/car_images/car45.jpg');
INSERT INTO `car` VALUES (46, 2022, 239500.00, '红色', 9500, '有货', 6, 'https://example.com/car_images/car46.jpg');
INSERT INTO `car` VALUES (47, 2023, 242600.00, '金色', 9700, '有货', 7, 'https://example.com/car_images/car47.jpg');
INSERT INTO `car` VALUES (48, 2021, 245700.00, '棕色', 9900, '有货', 8, 'https://example.com/car_images/car48.jpg');
INSERT INTO `car` VALUES (49, 2022, 248800.00, '绿色', 10100, '有货', 9, 'https://example.com/car_images/car49.jpg');
INSERT INTO `car` VALUES (50, 2023, 251900.00, '珍珠白', 10300, '有货', 10, 'https://example.com/car_images/car50.jpg');
INSERT INTO `car` VALUES (51, 2021, 255000.00, '白色', 10500, '有货', 11, 'https://example.com/car_images/car51.jpg');
INSERT INTO `car` VALUES (52, 2022, 258100.00, '黑色', 10700, '有货', 12, 'https://example.com/car_images/car52.jpg');
INSERT INTO `car` VALUES (53, 2023, 261200.00, '银色', 10900, '有货', 13, 'https://example.com/car_images/car53.jpg');
INSERT INTO `car` VALUES (54, 2021, 264300.00, '灰色', 11100, '有货', 14, 'https://example.com/car_images/car54.jpg');
INSERT INTO `car` VALUES (55, 2022, 267400.00, '蓝色', 11300, '有货', 15, 'https://example.com/car_images/car55.jpg');
INSERT INTO `car` VALUES (56, 2023, 270500.00, '红色', 11500, '有货', 16, 'https://example.com/car_images/car56.jpg');
INSERT INTO `car` VALUES (57, 2021, 273600.00, '金色', 11700, '有货', 17, 'https://example.com/car_images/car57.jpg');
INSERT INTO `car` VALUES (58, 2022, 276700.00, '棕色', 11900, '有货', 18, 'https://example.com/car_images/car58.jpg');
INSERT INTO `car` VALUES (59, 2023, 279800.00, '绿色', 12100, '有货', 19, 'https://example.com/car_images/car59.jpg');
INSERT INTO `car` VALUES (60, 2021, 282900.00, '珍珠白', 12300, '有货', 20, 'https://example.com/car_images/car60.jpg');
INSERT INTO `car` VALUES (61, 2022, 286000.00, '白色', 12500, '有货', 1, 'https://example.com/car_images/car61.jpg');
INSERT INTO `car` VALUES (62, 2023, 289100.00, '黑色', 12700, '有货', 2, 'https://example.com/car_images/car62.jpg');
INSERT INTO `car` VALUES (63, 2021, 292200.00, '银色', 12900, '有货', 3, 'https://example.com/car_images/car63.jpg');
INSERT INTO `car` VALUES (64, 2022, 295300.00, '灰色', 13100, '有货', 4, 'https://example.com/car_images/car64.jpg');
INSERT INTO `car` VALUES (65, 2023, 298400.00, '蓝色', 13300, '有货', 5, 'https://example.com/car_images/car65.jpg');
INSERT INTO `car` VALUES (66, 2021, 301500.00, '红色', 13500, '有货', 6, 'https://example.com/car_images/car66.jpg');
INSERT INTO `car` VALUES (67, 2022, 304600.00, '金色', 13700, '有货', 7, 'https://example.com/car_images/car67.jpg');
INSERT INTO `car` VALUES (68, 2023, 307700.00, '棕色', 13900, '有货', 8, 'https://example.com/car_images/car68.jpg');
INSERT INTO `car` VALUES (69, 2021, 310800.00, '绿色', 14100, '有货', 9, 'https://example.com/car_images/car69.jpg');
INSERT INTO `car` VALUES (70, 2022, 313900.00, '珍珠白', 14300, '有货', 10, 'https://example.com/car_images/car70.jpg');
INSERT INTO `car` VALUES (71, 2023, 317000.00, '白色', 14500, '有货', 11, 'https://example.com/car_images/car71.jpg');
INSERT INTO `car` VALUES (72, 2021, 320100.00, '黑色', 14700, '有货', 12, 'https://example.com/car_images/car72.jpg');
INSERT INTO `car` VALUES (73, 2022, 323200.00, '银色', 14900, '有货', 13, 'https://example.com/car_images/car73.jpg');
INSERT INTO `car` VALUES (74, 2023, 326300.00, '灰色', 15100, '有货', 14, 'https://example.com/car_images/car74.jpg');
INSERT INTO `car` VALUES (75, 2021, 329400.00, '蓝色', 15300, '有货', 15, 'https://example.com/car_images/car75.jpg');
INSERT INTO `car` VALUES (76, 2022, 332500.00, '红色', 15500, '有货', 16, 'https://example.com/car_images/car76.jpg');
INSERT INTO `car` VALUES (77, 2023, 335600.00, '金色', 15700, '有货', 17, 'https://example.com/car_images/car77.jpg');
INSERT INTO `car` VALUES (78, 2021, 338700.00, '棕色', 15900, '有货', 18, 'https://example.com/car_images/car78.jpg');
INSERT INTO `car` VALUES (79, 2022, 341800.00, '绿色', 16100, '有货', 19, 'https://example.com/car_images/car79.jpg');
INSERT INTO `car` VALUES (80, 2023, 344900.00, '珍珠白', 16300, '有货', 20, 'https://example.com/car_images/car80.jpg');
INSERT INTO `car` VALUES (81, 2021, 348000.00, '白色', 16500, '有货', 1, 'https://example.com/car_images/car81.jpg');
INSERT INTO `car` VALUES (82, 2022, 351100.00, '黑色', 16700, '有货', 2, 'https://example.com/car_images/car82.jpg');
INSERT INTO `car` VALUES (83, 2023, 354200.00, '银色', 16900, '有货', 3, 'https://example.com/car_images/car83.jpg');
INSERT INTO `car` VALUES (84, 2021, 357300.00, '灰色', 17100, '有货', 4, 'https://example.com/car_images/car84.jpg');
INSERT INTO `car` VALUES (85, 2022, 360400.00, '蓝色', 17300, '有货', 5, 'https://example.com/car_images/car85.jpg');
INSERT INTO `car` VALUES (86, 2023, 363500.00, '红色', 17500, '有货', 6, 'https://example.com/car_images/car86.jpg');
INSERT INTO `car` VALUES (87, 2021, 366600.00, '金色', 17700, '有货', 7, 'https://example.com/car_images/car87.jpg');
INSERT INTO `car` VALUES (88, 2022, 369700.00, '棕色', 17900, '有货', 8, 'https://example.com/car_images/car88.jpg');
INSERT INTO `car` VALUES (89, 2023, 372800.00, '绿色', 18100, '有货', 9, 'https://example.com/car_images/car89.jpg');
INSERT INTO `car` VALUES (90, 2021, 375900.00, '珍珠白', 18300, '有货', 10, 'https://example.com/car_images/car90.jpg');
INSERT INTO `car` VALUES (91, 2022, 379000.00, '白色', 18500, '有货', 11, 'https://example.com/car_images/car91.jpg');
INSERT INTO `car` VALUES (92, 2023, 382100.00, '黑色', 18700, '有货', 12, 'https://example.com/car_images/car92.jpg');
INSERT INTO `car` VALUES (93, 2021, 385200.00, '银色', 18900, '有货', 13, 'https://example.com/car_images/car93.jpg');
INSERT INTO `car` VALUES (94, 2022, 388300.00, '灰色', 19100, '有货', 14, 'https://example.com/car_images/car94.jpg');
INSERT INTO `car` VALUES (95, 2023, 391400.00, '蓝色', 19300, '有货', 15, 'https://example.com/car_images/car95.jpg');
INSERT INTO `car` VALUES (96, 2021, 394500.00, '红色', 19500, '有货', 16, 'https://example.com/car_images/car96.jpg');
INSERT INTO `car` VALUES (97, 2022, 397600.00, '金色', 19700, '有货', 17, 'https://example.com/car_images/car97.jpg');
INSERT INTO `car` VALUES (98, 2023, 400700.00, '棕色', 19900, '有货', 18, 'https://example.com/car_images/car98.jpg');
INSERT INTO `car` VALUES (99, 2021, 403800.00, '绿色', 20100, '有货', 19, 'https://example.com/car_images/car99.jpg');
INSERT INTO `car` VALUES (100, 2022, 406900.00, '珍珠白', 20300, '有货', 20, 'https://example.com/car_images/car100.jpg');

-- ----------------------------
-- Table structure for car_model
-- ----------------------------
DROP TABLE IF EXISTS `car_model`;
CREATE TABLE `car_model`  (
  `model_id` int NOT NULL AUTO_INCREMENT COMMENT '车型ID',
  `model_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车型名称',
  `body_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车身类型',
  `power_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '动力类型',
  `seats_number` int NULL DEFAULT NULL COMMENT '座位数',
  `brand_id` int NOT NULL COMMENT '品牌ID (外键)',
  PRIMARY KEY (`model_id`) USING BTREE,
  INDEX `brand_id`(`brand_id` ASC) USING BTREE,
  CONSTRAINT `car_model_ibfk_1` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`brand_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '车型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of car_model
-- ----------------------------
INSERT INTO `car_model` VALUES (1, '卡罗拉', '轿车', '汽油', 5, 1);
INSERT INTO `car_model` VALUES (2, '凯美瑞', '轿车', '混合动力', 5, 1);
INSERT INTO `car_model` VALUES (3, 'RAV4荣放', 'SUV', '汽油', 5, 1);
INSERT INTO `car_model` VALUES (4, '高尔夫', '轿车', '汽油', 5, 2);
INSERT INTO `car_model` VALUES (5, '帕萨特', '轿车', '汽油', 5, 2);
INSERT INTO `car_model` VALUES (6, '途观L', 'SUV', '汽油', 5, 2);
INSERT INTO `car_model` VALUES (7, 'CR-V', 'SUV', '混合动力', 5, 3);
INSERT INTO `car_model` VALUES (8, '思域', '轿车', '汽油', 5, 3);
INSERT INTO `car_model` VALUES (9, '雅阁', '轿车', '混合动力', 5, 3);
INSERT INTO `car_model` VALUES (10, 'C级', '轿车', '汽油', 5, 4);
INSERT INTO `car_model` VALUES (11, 'E级', '轿车', '汽油', 5, 4);
INSERT INTO `car_model` VALUES (12, 'GLC', 'SUV', '汽油', 5, 4);
INSERT INTO `car_model` VALUES (13, '3系', '轿车', '汽油', 5, 5);
INSERT INTO `car_model` VALUES (14, '5系', '轿车', '汽油', 5, 5);
INSERT INTO `car_model` VALUES (15, 'X5', 'SUV', '汽油', 5, 5);
INSERT INTO `car_model` VALUES (16, 'A4L', '轿车', '汽油', 5, 6);
INSERT INTO `car_model` VALUES (17, 'A6L', '轿车', '汽油', 5, 6);
INSERT INTO `car_model` VALUES (18, 'Model 3', '轿车', '纯电动', 5, 7);
INSERT INTO `car_model` VALUES (19, 'Model Y', 'SUV', '纯电动', 5, 7);
INSERT INTO `car_model` VALUES (20, '轩逸', '轿车', '汽油', 5, 8);

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite`  (
  `favorite_id` int NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` int NOT NULL COMMENT '用户ID (外键)',
  `car_id` int NOT NULL COMMENT '车辆ID (外键)',
  `favorite_time` datetime NULL DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`favorite_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `car_id`(`car_id` ASC) USING BTREE,
  CONSTRAINT `favorite_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `favorite_ibfk_2` FOREIGN KEY (`car_id`) REFERENCES `car` (`car_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of favorite
-- ----------------------------
INSERT INTO `favorite` VALUES (9, 2, 1, NULL);
INSERT INTO `favorite` VALUES (10, 2, 2, NULL);
INSERT INTO `favorite` VALUES (12, 3, 4, NULL);
INSERT INTO `favorite` VALUES (15, 3, 7, NULL);

-- ----------------------------
-- Table structure for test_drive_appointment
-- ----------------------------
DROP TABLE IF EXISTS `test_drive_appointment`;
CREATE TABLE `test_drive_appointment`  (
  `appointment_id` int NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `user_id` int NOT NULL COMMENT '用户ID (外键)',
  `car_id` int NOT NULL COMMENT '车辆ID (外键)',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预约状态',
  `appointment_time` datetime NULL DEFAULT NULL COMMENT '预约时间',
  `contact_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系方式',
  PRIMARY KEY (`appointment_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `car_id`(`car_id` ASC) USING BTREE,
  CONSTRAINT `test_drive_appointment_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `test_drive_appointment_ibfk_2` FOREIGN KEY (`car_id`) REFERENCES `car` (`car_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '预约试驾表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_drive_appointment
-- ----------------------------
INSERT INTO `test_drive_appointment` VALUES (7, 3, 5, '待确认', '2025-05-27 15:19:00', NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, 'admin', '$2a$10$NQqrr1gvpFM/WlfCpILx/e/5YZ/eUz7LOYMuzc7CEAc1AFfYbToly', '', '');
INSERT INTO `user` VALUES (3, 'test', '$2a$10$A/B0qDLackUuCOrVkYSPXuDdZLZuZT3.X5P7a14axJj00I3652FwW', 'ewdw@qq.com', '654156');

SET FOREIGN_KEY_CHECKS = 1;
