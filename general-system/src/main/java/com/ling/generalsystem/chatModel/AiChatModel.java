package com.ling.generalsystem.chatModel;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;


/**
 * Author: guoqing.ling@wfjt.com
 * Date: 2025/07/01  18:53
 * package_name: com.ling.generalsystem.chatModel
 * classname : AiChatModel
 */
@Slf4j
@Component
public class AiChatModel {

    private ChatClient chatClient;

    @Resource
    private ToolCallback[] toolCallbacks;

    @Autowired
    public AiChatModel(MessageWindowChatMemory memory, ChatModel ollamaChatModel) {
        this.chatClient = ChatClient.builder(ollamaChatModel)
                .defaultOptions(
                        ChatOptions.builder()
                                .build()
                )
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build(),
                        SimpleLoggerAdvisor.builder().build()
                )
                .build();
    }

    public String getString(String userInput, String conversationId) {
        return chatClient
                .prompt(userInput)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .toolCallbacks(toolCallbacks)
//                .user(userInput)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();
    }

    public Flux<String> getStream(String userInput, String conversationId) {
        return chatClient
                .prompt(userInput)
                .toolCallbacks(toolCallbacks)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId)) // 对话历史记录添加到提示中))
                .stream()
                .content();
    }
}
