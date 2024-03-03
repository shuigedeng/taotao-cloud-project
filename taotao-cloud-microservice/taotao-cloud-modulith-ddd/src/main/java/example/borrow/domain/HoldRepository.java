package example.borrow.domain;

import org.jmolecules.architecture.hexagonal.SecondaryPort;

import java.util.List;
import java.util.Optional;

@SecondaryPort
public interface HoldRepository {

    Hold save(Hold hold);

//    Checkout save(Checkout checkout);

    Optional<Hold> findById(Hold.HoldId id);

    List<Hold> activeHolds();

//    List<Checkout> checkouts();
}
