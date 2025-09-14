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

package com.taotao.cloud.modulith.borrow.application;

import com.taotao.cloud.modulith.borrow.domain.Book;
import com.taotao.cloud.modulith.borrow.domain.BookPlacedOnHold;
import com.taotao.cloud.modulith.borrow.domain.BookRepository;
import com.taotao.cloud.modulith.borrow.domain.Hold;
import com.taotao.cloud.modulith.borrow.domain.HoldEventPublisher;
import com.taotao.cloud.modulith.borrow.domain.HoldRepository;
import com.taotao.cloud.modulith.catalog.BookAddedToCatalog;
import java.util.Optional;
import java.util.UUID;
import org.jmolecules.architecture.hexagonal.Port;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Port
@Service
@Transactional
public class CirculationDesk {

    private BookRepository books;
    private HoldRepository holds;
    private HoldEventPublisher eventPublisher;

    //	public CirculationDesk(
    //		BookRepository books, HoldRepository holds, HoldEventPublisher eventPublisher) {
    //		this.books = books;
    //		this.holds = holds;
    //		this.eventPublisher = eventPublisher;
    //	}

    public HoldDto placeHold(Hold.PlaceHold command) {
        books.findAvailableBook(command.inventoryNumber())
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        return HoldDto.from(
                Hold.placeHold(command).then(holds::save).then(eventPublisher::holdPlaced));
    }

    public Optional<HoldDto> locate(UUID holdId) {
        return holds.findById(new Hold.HoldId(holdId)).map(HoldDto::from);
    }

    //    public Checkout checkout(Checkout.CheckoutBook command) {
    //        var hold = holds.findById(command.holdId())
    //                .orElseThrow(() -> new IllegalArgumentException("Hold not found!"));
    //
    //        return hold.checkout(command.dateOfCheckout())
    //                .then(holds::save)
    //                .then(eventPublisher::bookCheckedOut);
    //    }

    @ApplicationModuleListener
    public void handle(BookPlacedOnHold event) {
        books.findAvailableBook(new Book.Barcode(event.inventoryNumber()))
                .map(Book::markOnHold)
                .map(books::save)
                .orElseThrow(() -> new IllegalArgumentException("Duplicate hold?"));
    }

    @ApplicationModuleListener
    public void handle(BookAddedToCatalog event) {
        Book.AddBook command =
                new Book.AddBook(
                        new Book.Barcode(event.inventoryNumber()), event.title(), event.isbn());
        books.save(Book.addBook(command));
    }

    //    @ApplicationModuleListener
    //    public void handle(Checkout.BookCheckedOut event) {
    //        books.findOnHoldBook(event.checkout().getBarcode())
    //                .map(Book::markCheckedOut)
    //                .map(books::save)
    //                .orElseThrow(() -> new IllegalArgumentException("Duplicate checkout?"));
    //    }
}
