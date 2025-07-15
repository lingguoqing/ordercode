package com.ling.generalsystem.memory;

import com.ling.generalsystem.entity.AiDialogue;
import com.ling.generalsystem.service.AiDialogueService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: guoqing.ling@wfjt.com
 * Date: 2025/07/02  7:53
 * package_name: com.ling.generalsystem.memory
 * classname : MyChatMemory
 */
@Slf4j
@Component
public class MyChatMemory extends InMemoryChatMemory {

    @Resource
    private AiDialogueService aiDialogueService;

    /**
     * 保存消息到数据库
     */
    public void add(String conversationId, List<Message> messages) {
        List<AiDialogue> aiDialogueList = new ArrayList<>();
        for (Message item : messages) {
            AiDialogue aiDialogue = new AiDialogue();
            aiDialogue.setConversationId(conversationId);
            aiDialogue.setContent(String.valueOf(item));
            aiDialogue.setMessageType(String.valueOf(item.getMessageType()));
            aiDialogue.setTextContent(item.getText());
            aiDialogue.setCreateTime(LocalDateTime.now());
            aiDialogueList.add(aiDialogue);
        }
        aiDialogueService.saveBatch(aiDialogueList);
    }

    /**
     * 从数据库获取最近N条历史消息
     */
    public List<Message> get(String conversationId, int lastN) {
        List<AiDialogue> dbList = aiDialogueService.getByConversationId(conversationId, lastN);
        List<Message> messages = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (AiDialogue dialogue : dbList) {
            String type = dialogue.getMessageType();
            String text = dialogue.getTextContent();
            Message msg;
            if ("USER".equalsIgnoreCase(type)) {
                msg = new org.springframework.ai.chat.messages.UserMessage(text);
            } else if ("ASSISTANT".equalsIgnoreCase(type)) {
                msg = new org.springframework.ai.chat.messages.AssistantMessage(text);
            } else if ("SYSTEM".equalsIgnoreCase(type)) {
                msg = new org.springframework.ai.chat.messages.SystemMessage(text);
            } else if ("TOOL_RESPONSE".equalsIgnoreCase(type) || "TOOLRESPONSE".equalsIgnoreCase(type)) {
                try {
                    java.util.List<org.springframework.ai.chat.messages.ToolResponseMessage.ToolResponse> toolResponses =
                            objectMapper.readValue(
                                    text,
                                    new TypeReference<java.util.List<org.springframework.ai.chat.messages.ToolResponseMessage.ToolResponse>>() {
                                    }
                            );
                    msg = new org.springframework.ai.chat.messages.ToolResponseMessage(toolResponses);
                } catch (Exception e) {
                    msg = new org.springframework.ai.chat.messages.ToolResponseMessage(java.util.Collections.emptyList());
                }
            } else {
                msg = new org.springframework.ai.chat.messages.UserMessage(text);
            }
            messages.add(msg);
        }
        return messages;
    }

    /**
     * 清空某个会话的所有历史消息（数据库删除）
     */
    public void clear(String conversationId) {
        // 逻辑删除所有该会话的消息
        aiDialogueService.remove(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AiDialogue>()
                        .eq("conversation_id", conversationId)
        );
    }
}
