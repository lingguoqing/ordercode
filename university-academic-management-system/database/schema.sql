-- ----------------------------
-- 数据库和表创建脚本
-- ----------------------------

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `university_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `university_db`;

-- ----------------------------
-- Table structure for class
-- 班级表
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '班级ID',
  `name` VARCHAR(255) NOT NULL COMMENT '班级名称',
  `enrollment_year` INT COMMENT '入学年份',
  `major` VARCHAR(255) COMMENT '专业',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班级表';

-- ----------------------------
-- Table structure for student
-- 学生表
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '学生学号',
  `name` VARCHAR(255) NOT NULL COMMENT '姓名',
  `gender` VARCHAR(10) COMMENT '性别',
  `age` INT COMMENT '年龄',
  `enrollment_date` DATE COMMENT '入学时间',
  `class_id` INT COMMENT '所属班级ID',
  `password` VARCHAR(255) NOT NULL COMMENT '登录密码',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`class_id`) REFERENCES `class`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生表';

-- ----------------------------
-- Table structure for teacher
-- 教师表
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '教师编号',
  `name` VARCHAR(255) NOT NULL COMMENT '姓名',
  `gender` VARCHAR(10) COMMENT '性别',
  `title` VARCHAR(255) COMMENT '职称',
  `phone` VARCHAR(255) COMMENT '联系电话',
  `password` VARCHAR(255) NOT NULL COMMENT '登录密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教师表';

-- ----------------------------
-- Table structure for course
-- 课程表
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '课程编号',
  `name` VARCHAR(255) NOT NULL COMMENT '课程名称',
  `credit` DECIMAL(3, 1) COMMENT '学分',
  `type` VARCHAR(255) COMMENT '课程类型',
  `class_time` VARCHAR(255) COMMENT '上课时间',
  `location` VARCHAR(255) COMMENT '上课地点',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程表';

-- ----------------------------
-- Table structure for student_course
-- 学生选课表 (成绩表)
-- ----------------------------
DROP TABLE IF EXISTS `student_course`;
CREATE TABLE `student_course` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `student_id` INT NOT NULL COMMENT '学生ID',
  `course_id` INT NOT NULL COMMENT '课程ID',
  `score` DECIMAL(4, 1) COMMENT '分数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_course` (`student_id`, `course_id`),
  FOREIGN KEY (`student_id`) REFERENCES `student`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生选课表';

-- ----------------------------
-- Table structure for teacher_course
-- 教师授课表
-- ----------------------------
DROP TABLE IF EXISTS `teacher_course`;
CREATE TABLE `teacher_course` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `teacher_id` INT NOT NULL COMMENT '教师ID',
  `course_id` INT NOT NULL COMMENT '课程ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_teacher_course` (`teacher_id`, `course_id`),
  FOREIGN KEY (`teacher_id`) REFERENCES `teacher`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教师授课表';

-- ----------------------------
-- Table structure for exam
-- 考试安排表
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `course_id` INT NOT NULL COMMENT '课程ID',
  `exam_time` DATETIME COMMENT '考试时间',
  `location` VARCHAR(255) COMMENT '考试地点',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考试安排表';

-- ----------------------------
-- Table structure for major
-- 专业表
-- ----------------------------
DROP TABLE IF EXISTS `major`;
CREATE TABLE `major` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL UNIQUE COMMENT '专业名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='专业表';

-- ----------------------------
-- Table structure for admin
-- 管理员表
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- ----------------------------
-- Records for admin
-- ----------------------------
INSERT INTO `admin` (`username`, `password`) VALUES ('admin', 'admin123'); 