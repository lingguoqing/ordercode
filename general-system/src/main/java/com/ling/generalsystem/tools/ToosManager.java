package com.ling.generalsystem.tools;

import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: guoqing.ling@wfjt.com
 * Date: 2025/07/09  11:32
 * package_name: com.ling.generalsystem.tools
 * classname : ToosManager
 */
@Configuration
public class ToosManager {

    @Bean
    ToolCallingManager toolCallingManager() {
        return ToolCallingManager.builder().build();
    }
}
