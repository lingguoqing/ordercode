package com.ling.generalsystem.service;

import com.ling.generalsystem.chatModel.AiImageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 图片合成服务
 * 提供多种图片合成功能
 */
@Slf4j
@Service
public class ImageSynthesisService {

    @Autowired
    private AiImageModel aiImageModel;

    /**
     * 图片到图片转换
     * 基于参考图片生成新图片
     */
    public ImageResponse generateFromReference(Resource referenceImage, String prompt) {
        log.info("开始图片到图片转换，提示词: {}", prompt);
        return aiImageModel.generateFromImage(referenceImage, prompt);
    }

    /**
     * 多图片融合
     * 将多张图片的元素融合成一张新图片
     */
    public ImageResponse blendImages(List<Resource> images, String prompt) {
        log.info("开始多图片融合，图片数量: {}, 提示词: {}", images.size(), prompt);
        return aiImageModel.compositeImages(images, prompt);
    }

    /**
     * 风格迁移
     * 将一张图片的内容与另一张图片的风格结合
     */
    public ImageResponse transferStyle(Resource contentImage, Resource styleImage) {
        log.info("开始风格迁移");
        return aiImageModel.styleTransfer(contentImage, styleImage);
    }

    /**
     * 图片编辑
     * 在现有图片基础上进行编辑和修改
     */
    public ImageResponse editImage(Resource originalImage, String editPrompt) {
        log.info("开始图片编辑，编辑提示: {}", editPrompt);
        String fullPrompt = "基于原图进行以下编辑: " + editPrompt;
        return aiImageModel.generateFromImage(originalImage, fullPrompt);
    }

    /**
     * 图片扩展
     * 扩展图片的边界或内容
     */
    public ImageResponse extendImage(Resource originalImage, String extensionPrompt) {
        log.info("开始图片扩展，扩展提示: {}", extensionPrompt);
        String fullPrompt = "扩展图片边界，添加以下内容: " + extensionPrompt;
        return aiImageModel.generateFromImage(originalImage, fullPrompt);
    }

    /**
     * 图片修复
     * 修复图片中的缺陷或移除不需要的元素
     */
    public ImageResponse repairImage(Resource damagedImage, String repairPrompt) {
        log.info("开始图片修复，修复提示: {}", repairPrompt);
        String fullPrompt = "修复图片中的问题: " + repairPrompt;
        return aiImageModel.generateFromImage(damagedImage, fullPrompt);
    }

    /**
     * 获取支持的合成功能
     */
    public Map<String, String> getSupportedFeatures() {
        return Map.of(
            "generate_from_reference", "基于参考图片生成新图片",
            "blend_images", "多图片融合",
            "style_transfer", "风格迁移",
            "edit_image", "图片编辑",
            "extend_image", "图片扩展",
            "repair_image", "图片修复"
        );
    }

    /**
     * 获取使用建议
     */
    public String getUsageTips() {
        return """
            图片合成使用建议:
            
            1. 图片到图片转换:
               - 提供清晰的参考图片
               - 使用具体的描述性提示词
               - 指定期望的风格和效果
            
            2. 多图片融合:
               - 选择风格相近的图片
               - 明确指定融合的方式和重点
               - 控制图片数量(建议2-4张)
            
            3. 风格迁移:
               - 内容图片应包含清晰的主体
               - 风格图片应具有明显的艺术特征
               - 可以指定迁移的强度
            
            4. 图片编辑:
               - 明确描述要修改的部分
               - 指定修改后的效果
               - 保持与原图风格的一致性
            
            5. 图片扩展:
               - 描述要添加的内容
               - 考虑与原图的连贯性
               - 指定扩展的方向和范围
            
            6. 图片修复:
               - 明确描述需要修复的问题
               - 指定修复后的效果
               - 保持图片的整体风格
            """;
    }
} 