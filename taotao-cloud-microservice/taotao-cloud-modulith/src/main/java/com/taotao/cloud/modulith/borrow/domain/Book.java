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

package com.taotao.cloud.modulith.borrow.domain;

import java.util.UUID;
import lombok.Getter;
import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.ddd.types.ValueObject;

@Getter
public class Book {

    private final BookId id;

    private final Barcode inventoryNumber;

    private final String title;

    private final String isbn;

    private BookStatus status;

    private Book(AddBook addBook) {
        this.id = new BookId(UUID.randomUUID());
        this.inventoryNumber = addBook.barcode();
        this.title = addBook.title();
        this.isbn = addBook.isbn();
        this.status = BookStatus.AVAILABLE;
    }

    private Book(BookId id, Barcode inventoryNumber, String title, String isbn, BookStatus status) {
        this.id = id;
        this.inventoryNumber = inventoryNumber;
        this.title = title;
        this.isbn = isbn;
        this.status = status;
    }

    public static Book toBook(
            BookId id, Barcode inventoryNumber, String title, String isbn, BookStatus status) {
        return new Book(id, inventoryNumber, title, isbn, status);
    }

    public static Book addBook(AddBook command) {
        return new Book(command);
    }

    public Book markOnHold() {
        this.status = BookStatus.ON_HOLD;
        return this;
    }

    public record BookId(UUID id) implements Identifier {}

    public record Barcode(String barcode) implements ValueObject {}

    public enum BookStatus implements ValueObject {
        AVAILABLE,
        ON_HOLD,
        ISSUED
    }

    /**
     * Command to add a new book
     */
    public record AddBook(Barcode barcode, String title, String isbn) {}
}
