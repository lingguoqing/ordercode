package com.ling.generalsystem.config;

import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.ImageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI图片模型配置
 * 支持多种图片合成模型
 */
@Slf4j
@Configuration
public class ImageModelConfig {

    @Value("${spring.ai.dash-scope.api-key}")
    private String dashScopeApiKey;

    /**
     * 配置通义万相模型
     */
    @Bean
    public ImageModel dashScopeImageModel() {
        return new DashScopeImageModel(dashScopeApiKey);
    }

    /**
     * 支持的AI图片模型列表
     */
    public enum SupportedImageModels {
        // 阿里云通义万相
        DASHSCOPE_WANX("dashscope-wanx", "通义万相", "阿里云"),
        
        // OpenAI DALL-E
        OPENAI_DALLE("openai-dalle", "DALL-E", "OpenAI"),
        
        // Stable Diffusion
        STABLE_DIFFUSION("stable-diffusion", "Stable Diffusion", "开源"),
        
        // Midjourney (需要API代理)
        MIDJOURNEY("midjourney", "Midjourney", "Midjourney"),
        
        // 百度文心一格
        BAIDU_WENXIN("baidu-wenxin", "文心一格", "百度"),
        
        // 智谱AI
        ZHIPU_AI("zhipu-ai", "智谱AI", "智谱AI");

        private final String code;
        private final String name;
        private final String provider;

        SupportedImageModels(String code, String name, String provider) {
            this.code = code;
            this.name = name;
            this.provider = provider;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getProvider() {
            return provider;
        }
    }

    /**
     * 获取模型信息
     */
    public static String getModelInfo() {
        StringBuilder info = new StringBuilder();
        info.append("支持的AI图片合成模型:\n");
        
        for (SupportedImageModels model : SupportedImageModels.values()) {
            info.append(String.format("- %s (%s) - %s\n", 
                model.getName(), model.getCode(), model.getProvider()));
        }
        
        return info.toString();
    }
} 