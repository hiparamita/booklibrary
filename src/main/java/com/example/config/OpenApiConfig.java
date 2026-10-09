package com.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Book Library API",
        version = "1.0.0",
        description = """
                REST API for managing a book catalogue.
                1. Call **POST /api/v1/auth/login** (or register) to obtain a JWT.
                2. Click **Authorize** and paste the token.
                Reads require any authenticated user; writes require the ADMIN role.""",
        contact = @Contact(name = "Library API Team"),
        license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT")))
@SecurityScheme(
        name = OpenApiConfig.BEARER_AUTH,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
public class OpenApiConfig {
    public static final String BEARER_AUTH = "bearerAuth";
}