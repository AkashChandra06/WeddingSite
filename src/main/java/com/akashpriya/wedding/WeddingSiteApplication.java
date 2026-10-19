package com.akashpriya.wedding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Entry point for the Akash & Priya Wedding Celebration website.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class WeddingSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeddingSiteApplication.class, args);
    }
}

