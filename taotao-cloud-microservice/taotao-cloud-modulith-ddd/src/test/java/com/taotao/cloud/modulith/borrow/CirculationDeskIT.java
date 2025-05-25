package com.taotao.cloud.modulith.borrow;

import com.taotao.cloud.modulith.borrow.application.CirculationDesk;
import com.taotao.cloud.modulith.borrow.domain.*;
import com.taotao.cloud.modulith.borrow.domain.Patron.PatronId;
import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
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
	HoldEventPublisher publisher;

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
				(event, dto) -> Assertions.assertEquals("13268510", event.inventoryNumber()));
	}

	@Test
	void bookStatusIsUpdatedWhenPlacedOnHold(Scenario scenario) {
		BookPlacedOnHold event = new BookPlacedOnHold(UUID.randomUUID(), "64321704", LocalDate.now());
		scenario.publish(() -> event)
			.customize(it -> it.atMost(Duration.ofMillis(200)))
			.andWaitForStateChange(() -> books.findByBarcode("64321704"))
			.andVerify(book -> {
				//Assertions.assertThat(book).isNotEmpty();
				//Assertions.assertThat(book.get().getInventoryNumber().barcode()).isEqualTo("64321704");
				//Assertions.assertThat(book.get().getStatus()).isEqualTo(Book.BookStatus.ON_HOLD);
			});
	}
}
