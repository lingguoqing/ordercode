-- 创建学生表
CREATE TABLE student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    student_id VARCHAR(20) NOT NULL COMMENT '学号',
    student_name VARCHAR(50) NOT NULL COMMENT '学生姓名',
    gender VARCHAR(10) COMMENT '性别',
    class_name VARCHAR(50) COMMENT '班级',
    admission_date DATE COMMENT '入学日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_student_id (student_id)
) COMMENT '学生信息表';

-- 创建课程表
CREATE TABLE course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    course_id VARCHAR(20) NOT NULL COMMENT '课程编号',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    credit DECIMAL(3,1) NOT NULL COMMENT '学分',
    course_type VARCHAR(20) COMMENT '课程类型（必修/选修）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_course_id (course_id)
) COMMENT '课程信息表';

-- 创建成绩表
CREATE TABLE score (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    student_id VARCHAR(20) NOT NULL COMMENT '学号',
    course_id VARCHAR(20) NOT NULL COMMENT '课程编号',
    score DECIMAL(5,2) COMMENT '成绩',
    exam_date DATE COMMENT '考试日期',
    semester VARCHAR(20) COMMENT '学期',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (student_id) REFERENCES student(student_id),
    FOREIGN KEY (course_id) REFERENCES course(course_id),
    UNIQUE KEY uk_student_course (student_id, course_id, semester)
) COMMENT '成绩信息表';

-- 插入示例数据
INSERT INTO student (student_id, student_name, gender, class_name, admission_date) VALUES
('2024001', '张三', '男', '计算机1班', '2024-09-01'),
('2024002', '李四', '女', '计算机1班', '2024-09-01'),
('2024003', '王五', '男', '计算机2班', '2024-09-01');

INSERT INTO course (course_id, course_name, credit, course_type) VALUES
('CS001', 'Java程序设计', 4.0, '必修'),
('CS002', '数据库原理', 3.0, '必修'),
('CS003', 'Web开发', 3.0, '选修');

INSERT INTO score (student_id, course_id, score, exam_date, semester) VALUES
('2024001', 'CS001', 85.5, '2024-12-20', '2024-2025-1'),
('2024001', 'CS002', 92.0, '2024-12-22', '2024-2025-1'),
('2024002', 'CS001', 78.5, '2024-12-20', '2024-2025-1'),
('2024002', 'CS003', 88.0, '2024-12-25', '2024-2025-1'),
('2024003', 'CS002', 95.5, '2024-12-22', '2024-2025-1'); 