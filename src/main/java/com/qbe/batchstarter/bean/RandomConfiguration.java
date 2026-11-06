package com.qbe.batchstarter.bean;

import com.qbe.batchstarter.exception.InvalidRandomException;
import java.security.SecureRandom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RandomConfiguration {

    @Bean
    public SecureRandom random() {
        try {
            return SecureRandom.getInstanceStrong();
        } catch (Exception e) {
            throw new InvalidRandomException("Failed to create SecureRandom instance");
        }
    }
}
