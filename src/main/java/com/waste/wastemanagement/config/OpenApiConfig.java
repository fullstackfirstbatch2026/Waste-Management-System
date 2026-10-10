package com.waste.wastemanagement.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Waste Management System API",
        version = "1.0",
        description = "REST API for managing zones, households, workers, and waste collection records."
    )
)
public class OpenApiConfig {
}
