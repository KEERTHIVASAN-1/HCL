package com.hclhostel.config;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StartupLinks implements ApplicationListener<WebServerInitializedEvent> {
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        String contextPath = event.getApplicationContext().getEnvironment()
                .getProperty("server.servlet.context-path", "");
        int port = event.getWebServer().getPort();
        String baseUrl = "http://localhost:" + port + contextPath;

        System.out.println();
        System.out.println("Hostel Management Portal is ready:");
        System.out.println("Sign in: " + baseUrl + "/login");
        System.out.println();
    }
}
