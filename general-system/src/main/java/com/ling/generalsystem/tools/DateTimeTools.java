package com.ling.generalsystem.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;


@Slf4j
public class DateTimeTools {

    @Tool(description = "Get the current date and time in the user's timezone", returnDirect = true)
    String getCurrentDateTime() {
        log.info("获取当前时间：{}", LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString());
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

}