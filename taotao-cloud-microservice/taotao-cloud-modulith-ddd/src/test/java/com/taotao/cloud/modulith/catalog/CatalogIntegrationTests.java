package com.taotao.cloud.modulith.catalog;


import com.taotao.cloud.modulith.catalog.application.BookDto;
import com.taotao.cloud.modulith.catalog.application.CatalogManagement;
import com.taotao.cloud.modulith.catalog.domain.CatalogBook.Barcode;
import com.taotao.cloud.modulith.catalog.domain.CatalogRepository;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ApplicationModuleTest
class CatalogIntegrationTests {

    @DynamicPropertySource
    static void initializeData(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.data-locations", () -> "classpath:catalog_books.sql");
    }

    @Autowired
	CatalogManagement books;

    @Autowired
	CatalogRepository repository;

    @Test
    void shouldAddBookToInventory(Scenario scenario) {
        scenario.stimulate(() -> books.addToCatalog("A title", new Barcode("999"), "654", "An author"))
                .andCleanup(bookDto -> repository.deleteById(bookDto.id()))
                .andWaitForEventOfType(BookAddedToCatalog.class)
                .toArriveAndVerify((event, dto) -> {
                    //Assertions.assertThat(event.title()).isEqualTo("A title");
                    //Assertions.assertThat(event.inventoryNumber()).isEqualTo("999");
                    //Assertions.assertThat(event.isbn()).isEqualTo("654");
                    //Assertions.assertThat(event.author()).isEqualTo("An author");
                    //Assertions.assertThat(dto.id()).isNotNull();
                });
    }

    @Test
    void shouldListBooks() {
		List<BookDto> issuedBooks = books.fetchBooks();
		//Assertions.assertThat(issuedBooks).hasSizeBetween(3, 4);
    }
}
