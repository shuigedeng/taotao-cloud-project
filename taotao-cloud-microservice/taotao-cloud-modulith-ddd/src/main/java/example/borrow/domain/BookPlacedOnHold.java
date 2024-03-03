package example.borrow.domain;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.LocalDate;
import java.util.UUID;

@DomainEvent
public record BookPlacedOnHold(UUID holdId,
                               String inventoryNumber,
                               LocalDate dateOfHold) {
}
