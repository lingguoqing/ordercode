package com.ling.aidemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@SpringBootTest
class AiDemoApplicationTests {

    @Value("${uploadFile.path}")
    private String path;

    @Test
    void contextLoads() {
        ClassPathResource classPathResource = new ClassPathResource("/multimodal.test.png");
    }

}
