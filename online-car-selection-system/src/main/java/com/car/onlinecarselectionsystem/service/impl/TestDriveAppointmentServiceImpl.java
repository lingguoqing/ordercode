package com.car.onlinecarselectionsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.car.onlinecarselectionsystem.entity.TestDriveAppointment;
import com.car.onlinecarselectionsystem.mapper.TestDriveAppointmentMapper;
import com.car.onlinecarselectionsystem.service.TestDriveAppointmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TestDriveAppointmentServiceImpl extends ServiceImpl<TestDriveAppointmentMapper, TestDriveAppointment> implements TestDriveAppointmentService {
    // ServiceImpl已经提供了基本的CRUD方法，如果需要可以添加额外的业务逻辑
} 