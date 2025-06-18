package com.car.onlinecarselectionsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.car.onlinecarselectionsystem.entity.TestDriveAppointment;
import com.car.onlinecarselectionsystem.response.ApiResponse;
import com.car.onlinecarselectionsystem.service.TestDriveAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-drive-appointments")
public class TestDriveAppointmentController {

    @Autowired
    private TestDriveAppointmentService testDriveAppointmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<TestDriveAppointment>> createAppointment(@RequestBody TestDriveAppointment appointment) {
        try {
            // 设置默认状态
            if (appointment.getStatus() == null || appointment.getStatus().isEmpty()) {
                appointment.setStatus("待确认"); // 设置默认状态
            }
            testDriveAppointmentService.save(appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("预约创建成功", appointment));
        } catch (Exception e) {
            // 捕获所有异常，并返回统一的错误响应
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("创建预约失败: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TestDriveAppointment>>> getAllAppointments() {
        List<TestDriveAppointment> appointments = testDriveAppointmentService.list();
        return ResponseEntity.ok(ApiResponse.success(appointments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TestDriveAppointment>> getAppointmentById(@PathVariable Integer id) {
        TestDriveAppointment appointment = testDriveAppointmentService.getById(id);
        if (appointment != null) {
            return ResponseEntity.ok(ApiResponse.success(appointment));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("该数据不存在"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TestDriveAppointment>> updateAppointment(@PathVariable Integer id, @RequestBody TestDriveAppointment appointment) {
        appointment.setAppointmentId(id);
        if (testDriveAppointmentService.updateById(appointment)) {
            return ResponseEntity.ok(ApiResponse.success("更新成功", appointment));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("更新失败"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAppointment(@PathVariable Integer id) {
        if (testDriveAppointmentService.removeById(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success("删除成功"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("删除失败"));
        }
    }

    @GetMapping("/user/{userId}/car/{carId}")
    public ResponseEntity<ApiResponse<List<TestDriveAppointment>>> getAppointmentByUserIdAndCarId(@PathVariable Integer userId, @PathVariable Integer carId) {
        QueryWrapper<TestDriveAppointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("car_id", carId);
        List<TestDriveAppointment> appointments = testDriveAppointmentService.list(queryWrapper);
        return ResponseEntity.ok(ApiResponse.success(appointments));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TestDriveAppointment>>> getAppointmentsByUserId(@PathVariable Integer userId) {
        QueryWrapper<TestDriveAppointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<TestDriveAppointment> appointments = testDriveAppointmentService.list(queryWrapper);
        return ResponseEntity.ok(ApiResponse.success(appointments));
    }
} 