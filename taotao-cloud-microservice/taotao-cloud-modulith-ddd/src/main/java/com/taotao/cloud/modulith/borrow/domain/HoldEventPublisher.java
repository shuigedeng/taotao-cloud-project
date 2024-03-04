package com.taotao.cloud.modulith.borrow.domain;

import org.jmolecules.architecture.hexagonal.SecondaryPort;

@SecondaryPort
public interface HoldEventPublisher {

    void holdPlaced(BookPlacedOnHold event);

    default Hold holdPlaced(Hold hold) {
        BookPlacedOnHold event = new BookPlacedOnHold(hold.getId().id(), hold.getOnBook().barcode(), hold.getDateOfHold());
        this.holdPlaced(event);
        return hold;
    }

//    void bookCheckedOut(Checkout.BookCheckedOut event);
//
//    default Checkout bookCheckedOut(Checkout checkout) {
//        Checkout.BookCheckedOut event = new Checkout.BookCheckedOut(checkout);
//        this.bookCheckedOut(event);
//        return checkout;
//    }
}
