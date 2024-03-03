package example.borrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

import example.borrow.application.CirculationDesk;
import example.borrow.domain.Book;
import example.borrow.domain.BookPlacedOnHold;
import example.borrow.domain.BookRepository;
import example.borrow.domain.Hold;
import example.borrow.domain.HoldRepository;
import example.borrow.domain.Patron.PatronId;
import example.borrow.infrastructure.out.events.BorrowEventsPublisher;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ApplicationModuleTest
class CirculationDeskIT {

    @DynamicPropertySource
    static void initializeData(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.data-locations", () -> "classpath:borrow.sql");
    }

    @Autowired
    BookRepository books;

    @Autowired
    HoldRepository holds;

    @Autowired
    BorrowEventsPublisher publisher;

    CirculationDesk circulationDesk;

    @BeforeEach
    void setUp() {
        circulationDesk = new CirculationDesk(books, holds, publisher);
    }

    @Test
    void patronCanPlaceHold(Scenario scenario) {
        var command = new Hold.PlaceHold(new Book.Barcode("13268510"), LocalDate.now(), new PatronId(UUID.randomUUID()));
        scenario.stimulate(() -> circulationDesk.placeHold(command))
                .andWaitForEventOfType(BookPlacedOnHold.class)
                .toArriveAndVerify((event, dto) -> assertThat(event.inventoryNumber()).isEqualTo("13268510"));
    }

    @Test
    void bookStatusIsUpdatedWhenPlacedOnHold(Scenario scenario) {
        var event = new BookPlacedOnHold(UUID.randomUUID(), "64321704", LocalDate.now());
        scenario.publish(() -> event)
                .customize(it -> it.atMost(Duration.ofMillis(200)))
                .andWaitForStateChange(() -> books.findByBarcode("64321704"))
                .andVerify(book -> {
                    assertThat(book).isNotEmpty();
                    assertThat(book.get().getInventoryNumber().barcode()).isEqualTo("64321704");
                    assertThat(book.get().getStatus()).isEqualTo(Book.BookStatus.ON_HOLD);
                });
    }
}
