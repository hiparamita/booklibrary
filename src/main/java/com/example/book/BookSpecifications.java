package com.example.book;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class BookSpecifications {
    private BookSpecifications() {
    }
    static Specification<Book> matching(String title, String author, String genre) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            addContains(predicates, cb, root.<String>get("title"), title);
            addContains(predicates, cb, root.<String>get("author"), author);
            addContains(predicates, cb, root.<String>get("genre"), genre);
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
    private static void addContains(List<Predicate> predicates,
                                    jakarta.persistence.criteria.CriteriaBuilder cb,
                                    jakarta.persistence.criteria.Path<String> path,
                                    String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        String escaped = value.trim().toLowerCase(Locale.ROOT)
                .replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
        predicates.add(cb.like(cb.lower(path), "%" + escaped + "%", '\\'));
    }
}
