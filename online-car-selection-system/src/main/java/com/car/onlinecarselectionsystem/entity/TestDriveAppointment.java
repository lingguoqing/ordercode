package com.car.onlinecarselectionsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("test_drive_appointment")
public class TestDriveAppointment {
    @TableId(value = "appointment_id", type = IdType.AUTO)
    private Integer appointmentId;
    private Integer userId;
    private Integer carId;
    private String status;
    private LocalDateTime appointmentTime;
    private String contactInfo;
} 