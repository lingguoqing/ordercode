package com.ling.aidemo;// dashscope SDK的版本 >= 2.19.0
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import io.reactivex.Flowable;

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.exception.UploadFileException;
import com.alibaba.dashscope.exception.InputRequiredException;
import java.lang.System;
import com.alibaba.dashscope.utils.Constants;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static StringBuilder reasoningContent = new StringBuilder();
    private static StringBuilder finalContent = new StringBuilder();
    private static boolean isFirstPrint = true;

    private static void handleGenerationResult(MultiModalConversationResult message) {
        String re = message.getOutput().getChoices().get(0).getMessage().getReasoningContent();
        String reasoning = Objects.isNull(re)?"":re; // 默认值

        List<Map<String, Object>> content = message.getOutput().getChoices().get(0).getMessage().getContent();
        if (!reasoning.isEmpty()) {
            reasoningContent.append(reasoning);
            if (isFirstPrint) {
                System.out.println("====================思考过程====================");
                isFirstPrint = false;
            }
            System.out.print(reasoning);
        }

        if (Objects.nonNull(content) && !content.isEmpty()) {
            Object text = content.get(0).get("text");
            finalContent.append(content.get(0).get("text"));
            if (!isFirstPrint) {
                System.out.println("\n====================完整回复====================");
                isFirstPrint = true;
            }
            System.out.print(text);
        }
    }
    public static MultiModalConversationParam buildMultiModalConversationParam(MultiModalMessage Msg)  {
        return MultiModalConversationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey("sk-a831fb86ef4a4c2abcd8461114e96e3b")
                // 此处以 qvq-max 为例，可按需更换模型名称
                .model("qvq-max")
                .messages(Arrays.asList(Msg))
                .incrementalOutput(true)
                .build();
    }

    public static void streamCallWithMessage(MultiModalConversation conv, MultiModalMessage Msg)
            throws NoApiKeyException, ApiException, InputRequiredException, UploadFileException {
        MultiModalConversationParam param = buildMultiModalConversationParam(Msg);
        Flowable<MultiModalConversationResult> result = conv.streamCall(param);
        result.blockingForEach(message -> {
            handleGenerationResult(message);
        });
    }
    public static void main(String[] args) {
        try {
            MultiModalConversation conv = new MultiModalConversation();
            MultiModalMessage userMsg = MultiModalMessage.builder()
                    .role(Role.USER.getValue())
                    .content(Arrays.asList(Collections.singletonMap("image", "E:\\ordercode\\ai-demo\\src\\main\\resources\\multimodal.test.png"),
                            Collections.singletonMap("text", "图片中都有什么内容")))
                    .build();
            streamCallWithMessage(conv, userMsg);
//             打印最终结果
//            if (reasoningContent.length() > 0) {
//                System.out.println("\n====================完整回复====================");
//                System.out.println(finalContent.toString());
//            }
        } catch (ApiException | NoApiKeyException | UploadFileException | InputRequiredException e) {
            logger.error("An exception occurred: {}", e.getMessage());
        }
        System.exit(0);
    }
}