package com.ling.aidemo.chatModel;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.ling.aidemo.advisor.MyCustomSimpleLoggerAdvisor;
import com.ling.aidemo.memory.MyChatMemory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.image.ImageModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * Author: guoqing.ling@wfjt.com
 * Date: 2025/07/01  18:53
 * package_name: com.ling.aidemo.chatModel
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
    private ImageModel  dashscopeImageModel;

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
//                .defaultSystem()
                .build();
    }

    public String getString(String userInput, String conversationId) {
        dashscopeImageModel.call();
        return chatClient
                .prompt()
                .advisors(advisor -> advisor.param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .user(userInput)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getContent();
    }

    public Flux<ChatResponse> getStream(String userInput, String conversationId, String imgPath) {
        return chatClient
                .prompt()
                .advisors(advisor -> advisor.param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 5))
                .user(u -> {
                    u.text(userInput)
                            .media(MimeTypeUtils.IMAGE_PNG, new ClassPathResource("/multimodal.test.png"));

                })
                .stream()
                .chatResponse();
    }
}
