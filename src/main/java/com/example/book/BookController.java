package com.example.book;

import com.example.book.dto.BookRequest;
import com.example.book.dto.BookResponse;
import com.example.common.PageResponse;
import com.example.config.OpenApiConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
@Transactional(readOnly = true)
@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Books", description = "Browse and manage the catalogue")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
@ApiResponse(responseCode = "401", description = "Missing or invalid token")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "List books with optional filters, pagination and sorting",
            description = "Sortable fields: id, title, author, isbn, publicationYear, genre, availableCopies, createdAt. "
                    + "Max page size is 100.")
    public PageResponse<BookResponse> list(
            @Parameter(description = "Case-insensitive substring of the title") @RequestParam(required = false) String title,
            @Parameter(description = "Case-insensitive substring of the author") @RequestParam(required = false) String author,
            @Parameter(description = "Case-insensitive substring of the genre") @RequestParam(required = false) String genre,
            @ParameterObject @PageableDefault(size = 20, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        return bookService.search(title, author, genre, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by id")
    @ApiResponse(responseCode = "200", description = "Found")
    @ApiResponse(responseCode = "404", description = "Not found")
    public BookResponse get(@PathVariable Long id) {
        return bookService.get(id);
    }

    @PostMapping
    @Operation(summary = "Create a book (ADMIN)")
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @ApiResponse(responseCode = "403", description = "Requires ADMIN role")
    @ApiResponse(responseCode = "409", description = "ISBN already exists")
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
        BookResponse created = bookService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace a book (ADMIN)")
    @ApiResponse(responseCode = "200", description = "Updated")
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @ApiResponse(responseCode = "403", description = "Requires ADMIN role")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "409", description = "ISBN already used by another book")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return bookService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a book (ADMIN)")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiResponse(responseCode = "403", description = "Requires ADMIN role")
    @ApiResponse(responseCode = "404", description = "Not found")
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

}
