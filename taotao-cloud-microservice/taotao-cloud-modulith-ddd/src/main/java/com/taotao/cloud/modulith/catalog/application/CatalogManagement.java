/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.modulith.catalog.application;

import com.taotao.cloud.modulith.catalog.BookAddedToCatalog;
import com.taotao.cloud.modulith.catalog.domain.CatalogBook;
import com.taotao.cloud.modulith.catalog.domain.CatalogRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class CatalogManagement {

    private final CatalogRepository catalogRepository;
    private final BookMapper mapper;
    private final ApplicationEventPublisher events;

    /**
     * Add a new book to the library.
     */
    public BookDto addToCatalog(
            String title, CatalogBook.Barcode catalogNumber, String isbn, String authorName) {
        CatalogBook book =
                new CatalogBook(title, catalogNumber, isbn, new CatalogBook.Author(authorName));
        BookDto dto = mapper.toDto(catalogRepository.save(book));
        events.publishEvent(
                new BookAddedToCatalog(
                        dto.title(),
                        dto.catalogNumber().barcode(),
                        dto.isbn(),
                        dto.author().name()));
        return dto;
    }

    @Transactional(readOnly = true)
    public Optional<BookDto> locate(Long id) {
        return catalogRepository.findById(id).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<BookDto> fetchBooks() {
        return catalogRepository.findAll().stream().map(mapper::toDto).toList();
    }
}
