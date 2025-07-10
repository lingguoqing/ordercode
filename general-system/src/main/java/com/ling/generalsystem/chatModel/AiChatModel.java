package com.ling.generalsystem.chatModel;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.ling.generalsystem.advisor.MyCustomSimpleLoggerAdvisor;
import com.ling.generalsystem.memory.MyChatMemory;
import com.ling.generalsystem.tools.DateTimeTools;
import com.ling.generalsystem.tools.ToolRegistration;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
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
    private MyChatMemory myChatMemory;

    @Resource
    private ChatModel dashscopeChatModel;

    @Resource
    private ToolCallback[] toolCallbacks;

    @PostConstruct
    public void init() {
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultOptions(
                        DashScopeChatOptions.builder().withTopP(0.9).build()
                )
                .defaultAdvisors(new MyCustomSimpleLoggerAdvisor(),
//                        new MyReReadingAdvisor(),
                        MessageChatMemoryAdvisor.builder(myChatMemory).build() // 对话历史记录添加到提示中
//                        QuestionAnswerAdvisor.builder().build()
                )
                .defaultOptions(ChatOptions.builder().topK(40).build())
//                .defaultSystem()
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
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 1000))
                .user(userInput)
                .tools(toolCallbacks)
                .stream()
                .content();
    }
}
