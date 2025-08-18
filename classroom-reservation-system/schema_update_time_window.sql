-- Add per-classroom daily booking window (open/close time)
USE classroom_reservation;

ALTER TABLE classrooms
  ADD COLUMN IF NOT EXISTS open_time TIME NOT NULL DEFAULT '08:00:00',
  ADD COLUMN IF NOT EXISTS close_time TIME NOT NULL DEFAULT '22:00:00';


