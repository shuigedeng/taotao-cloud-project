package com.taotao.cloud.modulith.borrow.domain;

import java.util.List;
import java.util.Optional;
import org.jmolecules.architecture.hexagonal.SecondaryPort;

@SecondaryPort
public interface HoldRepository {

    Hold save(Hold hold);

//    Checkout save(Checkout checkout);

    Optional<Hold> findById(Hold.HoldId id);

    List<Hold> activeHolds();

//    List<Checkout> checkouts();
}
