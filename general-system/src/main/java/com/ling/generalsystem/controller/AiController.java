package com.ling.generalsystem.controller;

import com.ling.generalsystem.chatModel.AiChatModel;
import com.ling.generalsystem.tools.DateTimeTools;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RequestMapping
@RestController
class AiController {
    @Resource
    private AiChatModel aiChatModel;

    @Resource
    private OllamaChatModel ollamaChatModel;



    @GetMapping("/ai")
    String generation(String userInput, String conversationId) {
        String response = ChatClient.create(ollamaChatModel)
                .prompt("What day is tomorrow?")
                .tools(new DateTimeTools())
                .call()
                .content();
        System.out.println(response);
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
        return ChatClient.create(ollamaChatModel).prompt()
                .user(u -> u.text("Explain what do you see on this picture?")
                        .media(MimeTypeUtils.IMAGE_PNG, new ClassPathResource("./multimodal.test.png")))
                .call()
                .content();
    }


/*    @GetMapping("/message")
    public List<Message> message(String conversationId, int lastN) {
        return myChatMemory.get(conversationId, lastN);
    }*/

}