package com.taotao.cloud.modulith.borrow;

import static org.assertj.core.api.Assertions.assertThat;

import com.taotao.cloud.modulith.borrow.application.CirculationDesk;
import com.taotao.cloud.modulith.borrow.domain.Book;
import com.taotao.cloud.modulith.borrow.domain.BookPlacedOnHold;
import com.taotao.cloud.modulith.borrow.domain.BookRepository;
import com.taotao.cloud.modulith.borrow.domain.Hold;
import com.taotao.cloud.modulith.borrow.domain.HoldRepository;
import com.taotao.cloud.modulith.borrow.domain.Patron.PatronId;
import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

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
		Hold.PlaceHold command = new Hold.PlaceHold(new Book.Barcode("13268510"), LocalDate.now(),
			new PatronId(UUID.randomUUID()));
		scenario.stimulate(() -> circulationDesk.placeHold(command))
			.andWaitForEventOfType(BookPlacedOnHold.class)
			.toArriveAndVerify(
				(event, dto) -> assertThat(event.inventoryNumber()).isEqualTo("13268510"));
	}

	@Test
	void bookStatusIsUpdatedWhenPlacedOnHold(Scenario scenario) {
		BookPlacedOnHold event = new BookPlacedOnHold(UUID.randomUUID(), "64321704", LocalDate.now());
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
