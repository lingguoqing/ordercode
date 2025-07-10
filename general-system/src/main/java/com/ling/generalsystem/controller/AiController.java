package com.ling.generalsystem.controller;

import com.ling.generalsystem.chatModel.AiChatModel;
import com.ling.generalsystem.memory.MyChatMemory;
import com.ling.generalsystem.service.AiDialogueService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

@RequestMapping
@RestController
class AiController {

    @Resource
    private MyChatMemory myChatMemory;

    @Resource
    private AiChatModel aiChatModel;

    @Resource
    private ChatModel chatModel;

    @Resource
    private AiDialogueService aiDialogueService;

    @GetMapping("/ai")
    String generation(String userInput, String conversationId) {
        return aiChatModel.getString(userInput, conversationId);
    }

    @GetMapping(value = "/ai/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    SseEmitter stream(String userInput, String conversationId) {
        // 创建一个超时时间较长的 SseEmitter
        SseEmitter emitter = new SseEmitter(180000L); // 3分钟超时
        aiChatModel.getStream(userInput, conversationId).subscribe(
                // 处理每条消息
                chunk -> {
                    try {
                        emitter.send(chunk);
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                },
                // 处理错误
                emitter::completeWithError,
                // 处理完成
                emitter::complete
        );

        return emitter;
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

    @GetMapping("/message")
    public List<Message> message(String conversationId, int lastN) {
        return myChatMemory.get(conversationId, lastN);
    }

}