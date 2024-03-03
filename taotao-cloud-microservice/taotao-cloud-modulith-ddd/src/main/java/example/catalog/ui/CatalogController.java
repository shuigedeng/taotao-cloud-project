package example.catalog.ui;

import example.catalog.domain.CatalogBook.Barcode;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import example.catalog.application.BookDto;
import example.catalog.application.CatalogManagement;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class CatalogController {

    private final CatalogManagement books;

    @PostMapping("/catalog/books")
    ResponseEntity<BookDto> addBookToInventory(@RequestBody AddBookRequest request) {
        var bookDto = books.addToCatalog(request.title(), new Barcode(request.catalogNumber()), request.isbn(), request.author());
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
