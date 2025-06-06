package com.attendance.attendancedatasynchronization;

import com.attendance.attendancedatasynchronization.manager.SignManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class AttendanceDataSynchronizationApplicationTests {

    @Resource
    SignManager signManager;

    @Test
    void contextLoads() {
        signManager.getSign();
    }

}
