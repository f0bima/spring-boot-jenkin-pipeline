package com.f0bima.starter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Starter Backend API")
                        .version("1.0.0")
                        .description("Dokumentasi interaktif OpenAPI menggunakan Scalar UI untuk REST API Starter Spring Boot")
                        .contact(new Contact()
                                .name("f0bima Dev Team")
                                .email("dev@f0bima.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
