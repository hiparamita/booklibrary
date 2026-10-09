package com.example.book;

import com.example.book.dto.BookRequest;
import com.example.book.dto.BookResponse;
import com.example.common.PageResponse;
import com.example.exception.BadRequestException;
import com.example.exception.DuplicateResourceException;
import com.example.exception.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.Locale;
import java.util.Set;

public class BookService {
    private final BookRepository books;
    public BookService(BookRepository books) {
        this.books = books;
    }
    static final Set<String> SORTABLE_FIELDS =
            Set.of("id", "title", "author", "isbn", "publicationYear", "genre", "availableCopies", "createdAt");
    public PageResponse<BookResponse> search(String title, String author, String genre, Pageable pageable) {
        validateSort(pageable.getSort());
        return PageResponse.from(
                books.findAll(BookSpecifications.matching(title, author, genre), pageable).map(BookResponse::from));
    }


    @Transactional
    public BookResponse create(BookRequest request) {
        String isbn = normalizeIsbn(request.isbn());
        if (books.existsByIsbn(isbn)) {
            throw new DuplicateResourceException("A book with ISBN '%s' already exists".formatted(isbn));
        }
        Book book = new Book();
        apply(book, request, isbn);
        return BookResponse.from(books.save(book));
    }

    @Transactional
    public BookResponse update(Long id, BookRequest request) {
        Book book = find(id);
        String isbn = normalizeIsbn(request.isbn());
        if (books.existsByIsbnAndIdNot(isbn, id)) {
            throw new DuplicateResourceException("A book with ISBN '%s' already exists".formatted(isbn));
        }
        apply(book, request, isbn);
        return BookResponse.from(books.saveAndFlush(book));
    }

    @Transactional
    public void delete(Long id) {
        if (!books.existsById(id)) {
            throw new ResourceNotFoundException("Book not found");
        }
        books.deleteById(id);
    }

    /** Keeps only digits and the ISBN-10 check character, e.g. "978-0-13-235088-4" becomes "9780132350884". */
    static String normalizeIsbn(String isbn) {
        return isbn.replaceAll("[^0-9Xx]", "").toUpperCase(Locale.ROOT);
    }

    private Book find(Long id) {
        return books.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    private static void apply(Book book, BookRequest request, String normalizedIsbn) {
        book.setTitle(request.title().trim());
        book.setAuthor(request.author().trim());
        book.setIsbn(normalizedIsbn);
        book.setPublicationYear(request.publicationYear());
        book.setGenre(blankToNull(request.genre()));
        book.setDescription(blankToNull(request.description()));
        book.setAvailableCopies(request.availableCopies());
    }


    public BookResponse get(Long id) {
        return BookResponse.from(find(id));
    }
    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static void validateSort(Sort sort) {
        for (Sort.Order order : sort) {
            if (!SORTABLE_FIELDS.contains(order.getProperty())) {
                throw new BadRequestException("Cannot sort by '%s'. Allowed fields: %s"
                        .formatted(order.getProperty(), SORTABLE_FIELDS.stream().sorted().toList()));
            }
        }
    }
}
