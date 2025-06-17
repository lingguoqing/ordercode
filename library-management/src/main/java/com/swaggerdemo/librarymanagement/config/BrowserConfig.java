package com.swaggerdemo.librarymanagement.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.awt.Desktop;
import java.net.URI;

@Slf4j
@Configuration
public class BrowserConfig {

    @Resource
    private Environment environment;

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        try {
            String port = environment.getProperty("server.port", "9645");
            String contextPath = environment.getProperty("server.servlet.context-path", "");
            String url = String.format("http://localhost:%s%s", port, contextPath);
            
            log.info("Attempting to open browser with URL: {}", url);
            
            if (Desktop.isDesktopSupported()) {
                log.info("Desktop is supported");
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    log.info("BROWSE action is supported, opening browser...");
                    desktop.browse(new URI(url));
                    log.info("Browser opened successfully");
                } else {
                    log.warn("BROWSE action is not supported on this system");
                }
            } else {
                log.warn("Desktop is not supported on this system");
            }
        } catch (Exception e) {
            log.error("Failed to open browser", e);
        }
    }
} 