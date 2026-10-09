package com.example.book.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.ISBN;

public record BookRequest(@Schema(example = "Clean Architecture")
                          @NotBlank(message = "Title is required")
                          @Size(max = 255, message = "Title must be at most 255 characters")
                          String title,

                          @Schema(example = "Robert C. Martin")
                          @NotBlank(message = "Author is required")
                          @Size(max = 255, message = "Author must be at most 255 characters")
                          String author,

                          @Schema(example = "9780134494166", description = "ISBN-10 or ISBN-13; hyphens are allowed and stripped")
                          @NotBlank(message = "ISBN is required")
                          @ISBN(type = ISBN.Type.ANY, message = "ISBN must be a valid ISBN-10 or ISBN-13")
                          String isbn,

                          @Schema(example = "2017")
                          @Min(value = 1450, message = "Publication year must be 1450 or later")
                          @Max(value = 2100, message = "Publication year must be 2100 or earlier")
                          Integer publicationYear,

                          @Schema(example = "Software Architecture")
                          @Size(max = 100, message = "Genre must be at most 100 characters")
                          String genre,

                          @Schema(example = "A craftsman's guide to software structure and design.")
                          @Size(max = 2000, message = "Description must be at most 2000 characters")
                          String description,

                          @Schema(example = "3")
                          @NotNull(message = "Available copies is required")
                          @PositiveOrZero(message = "Available copies must be zero or greater")
                          Integer availableCopies) {
}
