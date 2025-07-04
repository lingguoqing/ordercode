package com.ling.aidemo.controller;

import com.ling.aidemo.chatModel.AiChatModel;
import com.ling.aidemo.memory.MyChatMemory;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RequestMapping
@RestController
class MyController {

    @Resource
    private MyChatMemory myChatMemory;

    @Resource
    private AiChatModel aiChatModel;

    @Resource
    private ChatModel chatModel;

    @GetMapping("/ai")
    String generation(String userInput, String conversationId) {
        return aiChatModel.getString(userInput, conversationId);
    }

    @GetMapping("/ai/stream")
    Flux<ChatResponse> stream(String userInput, String conversationId, String imgPath) {
        return aiChatModel.getStream(userInput, conversationId, imgPath);
    }

    @GetMapping("/test")
    public String test() {
        return ChatClient.create(chatModel).prompt()
                .user(u -> u.text("Explain what do you see on this picture?")
                        .media(MimeTypeUtils.IMAGE_PNG, new ClassPathResource("./multimodal.test.png")))
                .call()
                .content();
    }


    @GetMapping("/clear")
    public String clear(String conversationId) {
        myChatMemory.clear(conversationId);
        return "清楚成功";
    }

}