package com.sinaukoding.eventbooking.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Dino Darmayanto",
                        email = "dinodarmayanto22@gmail.com"
                ),
                description = "Swagger documentation for Aplikasi Event Booking",
                title = "Swagger Aplikasi Event Booking",
                version = "1.0.0",
                license = @License(
                        name = "Sinau Koding",
                        url = "https://sinaukoding.co.id/"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Development",
                        url = "http://localhost:8081/"
                ),
                @Server(
                        description = "Staging",
                        url = "http://localhost:8081/"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}
