package com.akashpriya.wedding.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Maps the physical uploads directory (outside the classpath) to the /uploads/** URL path
 * so guest uploaded photos/videos/voice notes can be served and viewed by admin & public pages.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${wedding.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + new File(uploadDir).getAbsolutePath() + File.separator;
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);
    }
}

