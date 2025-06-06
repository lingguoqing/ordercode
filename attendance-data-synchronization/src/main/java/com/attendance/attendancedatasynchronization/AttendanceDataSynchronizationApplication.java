package com.attendance.attendancedatasynchronization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class AttendanceDataSynchronizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceDataSynchronizationApplication.class, args);
    }

}
