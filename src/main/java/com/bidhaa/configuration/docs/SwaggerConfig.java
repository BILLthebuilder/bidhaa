package com.bidhaa.configuration.docs;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Bidhaa")
                        .description("A scalable Inventory Management System")
                        .version("v0.0.1")
                        .license(new License().name("GPL").url("https://github.com/BILLthebuilder/bidhaa/blob/main/LICENSE")))
                .externalDocs(new ExternalDocumentation()
                        .description("Bidhaa Wiki Documentation")
                        .url("https://github.com/BILLthebuilder/bidhaa/blob/main/README.md"));
    }

}

