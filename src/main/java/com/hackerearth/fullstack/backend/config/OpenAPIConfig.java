package com.hackerearth.fullstack.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server()
                .url("http://localhost:8000")
                .description("Local Development Server");
                
        return new OpenAPI()
                .info(new Info()
                        .title("DevFlowMetrics API")
                        .description("Event Tracking & Analytics System API Documentation")
                        .version("1.0")
                        .contact(new Contact()
                                .name("DevFlowMetrics Team")
                                .email("support@devflowmetrics.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(Arrays.asList(localServer));
    }
}