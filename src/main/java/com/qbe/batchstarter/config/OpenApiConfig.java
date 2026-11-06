package com.qbe.batchstarter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI batchStarterOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Batch Starter API")
                        .description("API de supervision et de gestion des traitements Spring Batch")
                        .version("1.0.0"));
    }
}
