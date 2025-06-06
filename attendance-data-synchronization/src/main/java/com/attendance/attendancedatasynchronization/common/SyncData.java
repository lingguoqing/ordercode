package com.attendance.attendancedatasynchronization.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sync")
public class SyncData {

    private String key;
    private String secret;
}
