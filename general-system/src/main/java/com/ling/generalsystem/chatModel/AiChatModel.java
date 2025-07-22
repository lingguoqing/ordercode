package com.ling.generalsystem.chatModel;

import com.ling.generalsystem.memory.MyChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
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

    private final ChatClient dashScopeChatModel;

    @Resource
    private ToolCallback[] toolCallbacks;

    @Autowired
    public AiChatModel(ChatModel dashScopeChatModel, JdbcChatMemoryRepository JdbcChatMemoryRepository) {

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(JdbcChatMemoryRepository)
                .maxMessages(10)
                .build();

        this.dashScopeChatModel = ChatClient.builder(dashScopeChatModel)
//                .defaultOptions(
//                        ChatOptions.builder()
//                                .build()
//                )
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory)
                )
                .build();
    }

    public String getString(String userInput, String conversationId) {
        return dashScopeChatModel
                .prompt(userInput)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .tools(toolCallbacks)
//                .user(userInput)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();
    }

    public Flux<String> getStream(String userInput, String conversationId) {
        return dashScopeChatModel
                .prompt(userInput)
                .tools(toolCallbacks)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))// 对话历史记录添加到提示中))
                .stream()
                .content();
    }
}
