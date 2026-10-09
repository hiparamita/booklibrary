package com.example.auth.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
@Schema(example = "Jane Reader")
@NotBlank(message = "Full name is required")
@Size(max = 100, message = "Full name must be at most 100 characters")
String fullName,

    @Schema(example = "jane@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid address")
    @Size(max = 255)
    String email,

    @Schema(example = "S3curePassw0rd")
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
    String password){

}

