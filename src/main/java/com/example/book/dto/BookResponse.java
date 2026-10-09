package com.example.book.dto;

import com.example.book.Book;
import java.time.Instant;

public record BookResponse(Long id,
                           String title,
                           String author,
                           String isbn,
                           Integer publicationYear,
                           String genre,
                           String description,
                           int availableCopies,
                           Instant createdAt,
                           Instant updatedAt) {
    public static BookResponse from(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(),
                book.getPublicationYear(), book.getGenre(), book.getDescription(), book.getAvailableCopies(),
                book.getCreatedAt(), book.getUpdatedAt());
    }
}
