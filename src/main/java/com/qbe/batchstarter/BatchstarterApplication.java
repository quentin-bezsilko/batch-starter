package com.qbe.batchstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BatchstarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchstarterApplication.class, args);
    }
}
