package com.ling.generalsystem.tools;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: guoqing.ling@wfjt.com
 * Date: 2025/07/09  13:44
 * package_name: com.ling.generalsystem.tools
 * classname : ToolCallback
 */
@Configuration
public class ToolRegistration {

    @Bean
    public ToolCallback[] toolCallbacks() {
        DateTimeTools dateTimeTools = new DateTimeTools();
        return ToolCallbacks.from(
                dateTimeTools
        );
    }
}
