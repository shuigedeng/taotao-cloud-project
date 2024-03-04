package com.taotao.cloud.modulith.borrow.domain;

import java.util.Optional;
import org.jmolecules.architecture.hexagonal.SecondaryPort;
import org.jmolecules.ddd.annotation.Repository;

@Repository
@SecondaryPort
public interface BookRepository {

    Optional<Book> findAvailableBook(Book.Barcode inventoryNumber);

    Optional<Book> findOnHoldBook(Book.Barcode inventoryNumber);

    Book save(Book book);

    Optional<Book> findByBarcode(String barcode);
}
