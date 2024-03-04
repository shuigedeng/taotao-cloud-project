package com.taotao.cloud.modulith.catalog.application;

import com.taotao.cloud.modulith.catalog.domain.CatalogBook;

public record BookDto(Long id, String title, CatalogBook.Barcode catalogNumber,
											String isbn, CatalogBook.Author author) {
}
