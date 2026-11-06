package com.qbe.batchstarter.config;

import static org.assertj.core.api.Assertions.assertThat;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

class OpenApiConfigTest {

    private final OpenApiConfig config = new OpenApiConfig();

    @Test
    void shouldCreateOpenApiConfiguration() {
        OpenAPI openAPI = config.batchStarterOpenAPI();
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo()).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("Batch Starter API");
        assertThat(openAPI.getInfo().getDescription())
                .isEqualTo("API de supervision et de gestion des traitements Spring Batch");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0.0");
    }
}
