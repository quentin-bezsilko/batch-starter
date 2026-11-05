package com.qbe.batchstarter.bean;

import java.util.Random;
import java.util.random.RandomGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RandomConfiguration {

    @Bean
    public Random random() {
        return Random.from(RandomGenerator.getDefault());
    }
}
