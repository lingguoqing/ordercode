-- Classroom Reservation System - MySQL schema
-- This script creates the database and all required tables.

CREATE DATABASE IF NOT EXISTS classroom_reservation
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE classroom_reservation;

-- Users (students/admins)
CREATE TABLE IF NOT EXISTS users (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  full_name VARCHAR(100) NOT NULL,
  email VARCHAR(120) NOT NULL,
  phone VARCHAR(32) NULL,
  password_hash VARCHAR(255) NOT NULL,
  role ENUM('user','admin') NOT NULL DEFAULT 'user',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Classrooms
CREATE TABLE IF NOT EXISTS classrooms (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  building VARCHAR(100) NOT NULL,
  location VARCHAR(100) NULL,
  capacity INT UNSIGNED NOT NULL,
  description TEXT NULL,
  is_featured TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Reservations
CREATE TABLE IF NOT EXISTS reservations (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id INT UNSIGNED NOT NULL,
  classroom_id INT UNSIGNED NOT NULL,
  activity VARCHAR(200) NOT NULL,
  reservation_date DATE NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  status ENUM('booked','cancelled') NOT NULL DEFAULT 'booked',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_reservations_room_date (classroom_id, reservation_date, start_time, end_time),
  CONSTRAINT fk_reservations_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_reservations_classroom
    FOREIGN KEY (classroom_id) REFERENCES classrooms(id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Seed featured classrooms (optional demo data)
INSERT INTO classrooms (name, building, location, capacity, description, is_featured)
VALUES
  ('A 栋 101', 'A 栋', 'A 栋 101 室', 40, '多媒体教室，适合讲座。', 1),
  ('B 栋 202', 'B 栋', 'B 栋 202 室', 30, '计算机房，含 30 台电脑。', 1),
  ('C 栋 303', 'C 栋', 'C 栋 303 室', 80, '大型报告厅。', 1),
  ('D 栋 404', 'D 栋', 'D 栋 404 室', 20, '小型研讨室。', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- Recent Events / Activities
CREATE TABLE IF NOT EXISTS events (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  sort_order INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO events (title, sort_order)
VALUES
  ('Web 应用程序开发讲座', 1),
  ('数据结构与算法研讨', 2)
ON DUPLICATE KEY UPDATE title=VALUES(title), sort_order=VALUES(sort_order);


