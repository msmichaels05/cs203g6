// src/main/java/com/yourpackage/config/WebConfig.java
package com.amateuraces.config; // replace 'yourpackage' with your actual package name

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Apply to all endpoints
                        .allowedOrigins("https://cs203g6-tennis.vercel.app/", "http://18.140.235.139/")  // Allow React frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Specify allowed HTTP methods
                        .allowCredentials(true);  // Allow cookies if necessary
            }
        };
    }
}
