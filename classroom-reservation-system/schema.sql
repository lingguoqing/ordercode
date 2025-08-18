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
  ('Building A 101', 'Building A', 'Building A Room 101', 40, 'Multimedia classroom, suitable for lectures.', 1),
  ('Building B 202', 'Building B', 'Building B Room 202', 30, 'Computer lab with 30 PCs.', 1),
  ('Building C 303', 'Building C', 'Building C Room 303', 80, 'Large auditorium.', 1),
  ('Building D 404', 'Building D', 'Building D Room 404', 20, 'Small seminar room.', 0)
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
  ('Web Application Development Talk', 1),
  ('Data Structures & Algorithms Seminar', 2)
ON DUPLICATE KEY UPDATE title=VALUES(title), sort_order=VALUES(sort_order);


