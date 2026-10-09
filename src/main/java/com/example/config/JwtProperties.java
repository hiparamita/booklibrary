package com.example.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
/**
 * JWT settings bound from {@code app.jwt.*}.
 *
 * @param secret     Base64-encoded HMAC-SHA key (at least 256 bits once decoded)
 * @param expiration token lifetime, ISO-8601 duration such as {@code PT1H}
 */
@Validated
@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(@NotBlank String secret, @NotNull Duration expiration) {
}