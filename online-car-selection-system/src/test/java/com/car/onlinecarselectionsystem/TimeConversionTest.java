package com.car.onlinecarselectionsystem;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConversionTest {

    @Test
    void testTimeConversion() {
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();

        // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化当前时间
        String formattedCurrentTime = currentTime.format(formatter);
        System.out.println("当前时间: " + formattedCurrentTime);

        // 获取墨西哥城时区时间
        // 墨西哥城时区ID: America/Mexico_City
        ZoneId mexicoCityZone = ZoneId.of("America/Mexico_City");
        ZonedDateTime mexicoCityTime = currentTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(mexicoCityZone);

        // 格式化墨西哥城时间
        String formattedMexicoCityTime = mexicoCityTime.format(formatter);
        System.out.println("墨西哥城时间: " + formattedMexicoCityTime);
    }
} 