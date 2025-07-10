package com.ling.generalsystem.chatModel;

import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * AI图片合成模型
 * 支持多种图片合成功能
 */
@Slf4j
@Component
public class AiImageModel {

    @Value("${spring.ai.dash-scope.api-key}")
    private String apiKey;

    private ImageModel imageModel;

    /**
     * 多图片合成 - 主要功能
     * @param images 多张参考图片
     * @param prompt 合成提示词
     * @return 合成后的图片
     */
    public ImageResponse compositeMultipleImages(List<Resource> images, String prompt) {
        log.info("开始多图片合成，图片数量: {}, 提示词: {}", images.size(), prompt);
        
        try {
            // 构建增强的提示词
            String enhancedPrompt = buildEnhancedPrompt(images, prompt);
            
            // 使用第一张图片作为基础图片
            Resource baseImage = images.get(0);
            
            // 配置通义万相模型参数
            DashScopeImageOptions options = DashScopeImageOptions.builder()
                    .withModel("wanx-v1")
                    .withStyle("vivid")
                    .withSize("1024*1024")
                    .withN(1) // 生成1张图片
                    .build();

            // 创建图片提示
            ImagePrompt imagePrompt = new ImagePrompt(enhancedPrompt, baseImage);
            
            // 调用模型生成
            return imageModel.call(imagePrompt);
            
        } catch (Exception e) {
            log.error("多图片合成失败", e);
            throw new RuntimeException("多图片合成失败: " + e.getMessage());
        }
    }

    /**
     * 图片混合合成
     * @param images 多张图片
     * @param blendPrompt 混合提示词
     * @return 混合后的图片
     */
    public ImageResponse blendImages(List<Resource> images, String blendPrompt) {
        log.info("开始图片混合，图片数量: {}", images.size());
        
        String prompt = "将以下多张图片的元素进行混合: " + blendPrompt;
        return compositeMultipleImages(images, prompt);
    }

    /**
     * 风格融合
     * @param contentImages 内容图片列表
     * @param styleImages 风格图片列表
     * @param fusionPrompt 融合提示词
     * @return 风格融合后的图片
     */
    public ImageResponse styleFusion(List<Resource> contentImages, 
                                   List<Resource> styleImages, 
                                   String fusionPrompt) {
        log.info("开始风格融合，内容图片: {}, 风格图片: {}", contentImages.size(), styleImages.size());
        
        // 合并所有图片
        List<Resource> allImages = new java.util.ArrayList<>(contentImages);
        allImages.addAll(styleImages);
        
        String prompt = "将内容图片的风格与风格图片结合: " + fusionPrompt;
        return compositeMultipleImages(allImages, prompt);
    }

    /**
     * 元素提取合成
     * @param images 多张图片
     * @param elementPrompt 元素提取提示词
     * @return 元素合成后的图片
     */
    public ImageResponse extractAndComposite(List<Resource> images, String elementPrompt) {
        log.info("开始元素提取合成，图片数量: {}", images.size());
        
        String prompt = "从多张图片中提取指定元素并合成: " + elementPrompt;
        return compositeMultipleImages(images, prompt);
    }

    /**
     * 构建增强的提示词
     */
    private String buildEnhancedPrompt(List<Resource> images, String basePrompt) {
        StringBuilder enhancedPrompt = new StringBuilder();
        enhancedPrompt.append("基于以下多张参考图片进行合成: ");
        enhancedPrompt.append(basePrompt);
        enhancedPrompt.append("。请融合所有图片的元素，创造出一张全新的图片。");
        
        // 根据图片数量添加具体指导
        if (images.size() == 2) {
            enhancedPrompt.append("将两张图片的元素进行平衡融合。");
        } else if (images.size() == 3) {
            enhancedPrompt.append("将三张图片的元素进行三角融合。");
        } else if (images.size() > 3) {
            enhancedPrompt.append("将多张图片的元素进行综合融合，保持视觉平衡。");
        }
        
        return enhancedPrompt.toString();
    }

    /**
     * 图片到图片转换
     * @param sourceImage 源图片
     * @param prompt 提示词
     * @return 生成的图片
     */
    public ImageResponse generateFromImage(Resource sourceImage, String prompt) {
        DashScopeImageOptions options = DashScopeImageOptions.builder()
                .withModel("wanx-v1") // 通义万相模型
                .withStyle("vivid") // 生动风格
                .withSize("1024*1024")
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(prompt, sourceImage);
        return imageModel.call(imagePrompt);
    }

    /**
     * 图片风格迁移
     * @param contentImage 内容图片
     * @param styleImage 风格图片
     * @return 风格迁移后的图片
     */
    public ImageResponse styleTransfer(Resource contentImage, Resource styleImage) {
        String prompt = "将内容图片的风格转换为参考图片的风格";
        return generateFromImage(contentImage, prompt);
    }
} 