package com.igrowker.wander.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Wander API Documentation",
                description = "Wander is a web app that connects tourists with local residents offering unique cultural experiences. " +
                        "The platform allows travelers to explore and book authentic activities, " +
                        "while providing locals a space to promote their services.",
                version = "1.0",
                contact = @Contact(
                        name = "Wander team",
                        url = "https://igrowker.com/",
                        email = "info@igrowker.com"
                )
        ),
        security = @SecurityRequirement(
        name = "BearerAuth"
))
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
