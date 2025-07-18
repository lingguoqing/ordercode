package com.ling.generalsystem.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Author: guoqing.ling@wfjt.com
 * Date: 2025/07/02  7:53
 * package_name: com.ling.generalsystem.memory
 * classname : MyChatMemory
 */
@Slf4j
@Component
public class MyChatMemory {

    @Autowired
    private JdbcChatMemoryRepository chatMemoryRepository;




    @Bean
    public MessageWindowChatMemory memory() {
        return MessageWindowChatMemory.builder().chatMemoryRepository(chatMemoryRepository).maxMessages(50).build();
    }

}
