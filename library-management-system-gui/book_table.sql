-- 创建数据库
CREATE DATABASE IF NOT EXISTS ordercode DEFAULT CHARSET utf8mb4;
USE ordercode;

-- 创建图书表
CREATE TABLE IF NOT EXISTS book
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    title     VARCHAR(255) NOT NULL,
    author    VARCHAR(255) NOT NULL,
    isbn      VARCHAR(50)  NOT NULL,
    stock     INT          NOT NULL,
    publisher VARCHAR(255)
);

-- 插入测试数据
INSERT INTO book (title, author, isbn, stock, publisher)
VALUES ('Java编程思想', 'Bruce Eckel', '9787111213826', 10, '机械工业出版社'),
       ('深入理解计算机系统', 'Randal E. Bryant', '9787111544937', 8, '机械工业出版社'),
       ('算法导论', 'Thomas
 H. Cormen', '9787111187776', 5, '机械工业出版社');