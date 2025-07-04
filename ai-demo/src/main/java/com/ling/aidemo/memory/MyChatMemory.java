package com.ling.aidemo.memory;

import com.ling.aidemo.entity.AiDialogue;
import com.ling.aidemo.service.AiDialogueService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: guoqing.ling@wfjt.com
 * Date: 2025/07/02  7:53
 * package_name: com.ling.aidemo.memory
 * classname : MyChatMemory
 */
@Slf4j
@Component
public class MyChatMemory extends InMemoryChatMemory {

    @Resource
    private AiDialogueService aiDialogueService;

    @Override
    public void add(String conversationId, List<Message> messages) {
        log.info("当前对话conversationId：{}", conversationId);
        List<AiDialogue> aiDialogues = new ArrayList<>();
        for (Message item : messages) {
            AiDialogue dialogue = new AiDialogue();
            dialogue.setConversationId(conversationId);
            dialogue.setContent(item.getContent());
            dialogue.setMessageType(item.getMessageType() != null ? item.getMessageType().getValue() : null);
            dialogue.setMetadata(item.getMetadata() != null ? item.getMetadata().toString() : null);
            dialogue.setCreateTime(LocalDateTime.now());
            // 其他字段可根据需要补充
            aiDialogues.add(dialogue);
        }
        aiDialogueService.saveBatch(aiDialogues);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<AiDialogue> all = aiDialogueService.lambdaQuery()
                .eq(AiDialogue::getConversationId, conversationId)
                .orderByDesc(AiDialogue::getCreateTime)
                .last("limit " + lastN)
                .list();
        return all.stream().map(this::toMessage).collect(Collectors.toList());
    }

    @Override
    public void clear(String conversationId) {
        log.info("清除操作：{}", conversationId);
        aiDialogueService.lambdaUpdate()
                .eq(AiDialogue::getConversationId, conversationId)
                .remove();
    }

    private Message toMessage(AiDialogue dialogue) {
        MessageType type = dialogue.getMessageType() != null ? MessageType.fromValue(dialogue.getMessageType()) : MessageType.USER;
        String content = dialogue.getContent();
        Map<String, Object> metadata = Map.of("metadata", dialogue.getMetadata());
        Message msg;
        switch (type) {
            case USER:
                msg = new org.springframework.ai.chat.messages.UserMessage(content);
                break;
            case ASSISTANT:
                msg = new org.springframework.ai.chat.messages.AssistantMessage(content);
                break;
            case SYSTEM:
                msg = new org.springframework.ai.chat.messages.SystemMessage(content);
                break;
            default:
                msg = new org.springframework.ai.chat.messages.UserMessage(content);
        }
        try {
            var method = msg.getClass().getMethod("setMetadata", Map.class);
            method.invoke(msg, metadata);
        } catch (Exception e) {
            // 没有setMetadata方法则忽略
        }
        return msg;
    }
}
