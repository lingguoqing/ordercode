package com.ling.generalsystem.chatModel;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.ling.generalsystem.advisor.MyCustomSimpleLoggerAdvisor;
import com.ling.generalsystem.memory.MyChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

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
    public AiChatModel(ChatModel dashscopeChatModel, MyChatMemory myChatMemory) {
        this.chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.1)
                                .withMaxToken(1)
                                .withRepetitionPenalty(0.5)
                                .withTemperature(0.1)
                                .build()
                )
                .defaultSystem("我是一个优秀的程序员，会java、python语言")
                .defaultAdvisors(new MyCustomSimpleLoggerAdvisor(),
                        new MessageChatMemoryAdvisor(myChatMemory)
                )
                .build();
    }

    public String getString(String userInput, String conversationId) {
        return chatClient
                .prompt()
                .advisors(advisor -> advisor.param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .user(userInput)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .toString();
    }

    public Flux<String> getStream(String userInput, String conversationId) {
        return chatClient
                .prompt()
                .advisors(advisor -> advisor.param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 1000)) // 对话历史记录添加到提示中))
                .user(userInput)
                .tools(toolCallbacks)
                .stream()
                .content();
    }
}
