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

package com.taotao.cloud.modulith.borrow;

import static com.taotao.cloud.modulith.borrow.domain.Book.BookStatus.AVAILABLE;
import static com.taotao.cloud.modulith.borrow.domain.Book.BookStatus.ON_HOLD;

import com.taotao.cloud.modulith.borrow.application.CirculationDesk;
import com.taotao.cloud.modulith.borrow.application.HoldDto;
import com.taotao.cloud.modulith.borrow.domain.Book;
import com.taotao.cloud.modulith.borrow.domain.BookPlacedOnHold;
import com.taotao.cloud.modulith.borrow.domain.BookRepository;
import com.taotao.cloud.modulith.borrow.domain.Hold;
import com.taotao.cloud.modulith.borrow.domain.HoldEventPublisher;
import com.taotao.cloud.modulith.borrow.domain.HoldRepository;
import com.taotao.cloud.modulith.borrow.domain.Patron.PatronId;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CirculationDeskTest {

    CirculationDesk circulationDesk;

    BookRepository bookRepository;

    HoldRepository holdRepository;

    @BeforeEach
    void setUp() {
        bookRepository = new InMemoryBooks();
        holdRepository = new InMemoryHolds();
        circulationDesk =
                new CirculationDesk(
                        bookRepository, holdRepository, new InMemoryHoldsEventPublisher());
    }

    @Test
    void patronCanPlaceHold() {
        Hold.PlaceHold command =
                new Hold.PlaceHold(
                        new Book.Barcode("12345"),
                        LocalDate.now(),
                        new PatronId(UUID.randomUUID()));
        HoldDto holdDto = circulationDesk.placeHold(command);
        // Assertions.assertThat(holdDto.getBookBarcode()).isEqualTo("12345");
        // Assertions.assertThat(holdDto.getDateOfHold()).isNotNull();
    }

    @Test
    void bookStatusUpdatedWhenPlacedOnHold() {
        Hold.PlaceHold command =
                new Hold.PlaceHold(
                        new Book.Barcode("12345"),
                        LocalDate.now(),
                        new PatronId(UUID.randomUUID()));
        Hold hold = Hold.placeHold(command);
        circulationDesk.handle(
                new BookPlacedOnHold(
                        hold.getId().id(), hold.getOnBook().barcode(), hold.getDateOfHold()));
        //noinspection OptionalGetWithoutIsPresent
        Book book = bookRepository.findByBarcode("12345").get();
        // Assertions.assertThat(book.getStatus()).isEqualTo(ON_HOLD);
    }

    //    @Test
    //    void patronCanCheckoutBook() {
    //        var command = new Hold.PlaceHold("12345", LocalDate.now());
    //        var hold = circulationDesk.placeHold(command);
    //        var checkout = circulationDesk.checkout(new Checkout.CheckoutBook(hold.getId(),
    // hold.getBarcode(), LocalDate.now()));
    //        assertThat(checkout.getBarcode()).isEqualTo("12345");
    //        assertThat(checkout.getHoldId()).isEqualTo(hold.getId());
    //        assertThat(checkout.getDateOfCheckout()).isNotNull();
    //    }

    //    @Test
    //    void bookStatusUpdatedWhenCheckedOut() {
    //        // place on hold
    //        var command = new Hold.PlaceHold("12345", LocalDate.now());
    //        var hold = circulationDesk.placeHold(command);
    //        // publish event
    //        circulationDesk.handle(new Hold.HoldPlaced(hold));
    //
    //        // checkout book
    //        var checkout = circulationDesk.checkout(new Checkout.CheckoutBook(hold.getId(),
    // hold.getBarcode(), LocalDate.now()));
    //        // publish event
    //        circulationDesk.handle(new Checkout.BookCheckedOut(checkout));
    //
    //        var book = bookRepository.findByBarcode("12345").get();
    //        assertThat(book.getStatus()).isEqualTo(ISSUED);
    //    }
}

class InMemoryBooks implements BookRepository {

    private final Map<String, Book> books = new HashMap<>();

    public InMemoryBooks() {
        var booksToAdd =
                List.of(
                        Book.addBook(
                                new Book.AddBook(
                                        new Book.Barcode("12345"), "A famous book", "92972947199")),
                        Book.addBook(
                                new Book.AddBook(
                                        new Book.Barcode("98765"),
                                        "Another famous book",
                                        "98137674132")));
        booksToAdd.forEach(book -> books.put(book.getInventoryNumber().barcode(), book));
    }

    @Override
    public Optional<Book> findAvailableBook(Book.Barcode barcode) {
        return books.values().stream()
                .filter(it -> it.getInventoryNumber().equals(barcode))
                .filter(it -> it.getStatus().equals(AVAILABLE))
                .findFirst();
    }

    @Override
    public Optional<Book> findOnHoldBook(Book.Barcode barcode) {
        return books.values().stream()
                .filter(it -> it.getInventoryNumber().equals(barcode))
                .filter(it -> it.getStatus().equals(ON_HOLD))
                .findFirst();
    }

    @Override
    public Book save(Book book) {
        books.put(book.getInventoryNumber().barcode(), book);
        return book;
    }

    @Override
    public Optional<Book> findByBarcode(String barcode) {
        return books.values().stream()
                .filter(it -> it.getInventoryNumber().barcode().equals(barcode))
                .findFirst();
    }
}

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
class InMemoryHolds implements HoldRepository {

    private final Map<UUID, Hold> holds = new HashMap<>();

    //    private final Map<UUID, Checkout> checkouts = new HashMap<>();

    public InMemoryHolds() {
        var hold =
                Hold.placeHold(
                        new Hold.PlaceHold(
                                new Book.Barcode("98765"),
                                LocalDate.now(),
                                new PatronId(UUID.randomUUID())));
        holds.put(hold.getId().id(), hold);
    }

    @Override
    public Hold save(Hold hold) {
        holds.put(hold.getId().id(), hold);
        return hold;
    }

    //    @Override
    //    public Checkout save(Checkout checkout) {
    //        checkouts.put(checkout.getHoldId().id(), checkout);
    //        return checkout;
    //    }

    @Override
    public Optional<Hold> findById(Hold.HoldId id) {
        return Optional.ofNullable(holds.get(id.id()));
    }

    @Override
    public List<Hold> activeHolds() {
        return holds.values().stream().toList();
    }

    //    @Override
    //    public List<Checkout> checkouts() {
    //        return checkouts.values().stream().toList();
    //    }
}

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
class InMemoryHoldsEventPublisher implements HoldEventPublisher {

    private final List<BookPlacedOnHold> events = new LinkedList<>();

    @Override
    public void holdPlaced(BookPlacedOnHold event) {
        events.add(event);
    }

    //    @Override
    //    public void bookCheckedOut(Checkout.BookCheckedOut event) {
    //        // NOOP
    //    }
}
