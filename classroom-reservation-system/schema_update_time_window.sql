-- 为教室添加每日可预约时间窗口（开放时间/结束时间）
USE classroom_reservation;

ALTER TABLE classrooms
  ADD COLUMN IF NOT EXISTS open_time TIME NOT NULL DEFAULT '08:00:00',
  ADD COLUMN IF NOT EXISTS close_time TIME NOT NULL DEFAULT '22:00:00';


