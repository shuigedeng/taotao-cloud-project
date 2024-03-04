package com.taotao.cloud.modulith.catalog.ui;

import com.taotao.cloud.modulith.catalog.application.BookDto;
import com.taotao.cloud.modulith.catalog.application.CatalogManagement;
import com.taotao.cloud.modulith.catalog.domain.CatalogBook.Barcode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class CatalogController {

    private final CatalogManagement books;

    @PostMapping("/catalog/books")
    ResponseEntity<BookDto> addBookToInventory(@RequestBody AddBookRequest request) {
		BookDto bookDto = books.addToCatalog(request.title(), new Barcode(request.catalogNumber()), request.isbn(), request.author());
        return ResponseEntity.ok(bookDto);
    }

    @GetMapping("/catalog/books/{id}")
    ResponseEntity<BookDto> viewSingleBook(@PathVariable("id") Long id) {
        return books.locate(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/catalog/books")
    ResponseEntity<List<BookDto>> viewBooks() {
        return ResponseEntity.ok(books.fetchBooks());
    }

    record AddBookRequest(String title, String catalogNumber,
                          String isbn, String author) {
    }
}
