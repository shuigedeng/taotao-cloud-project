package example.borrow.domain;

import org.jmolecules.architecture.hexagonal.SecondaryPort;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
@SecondaryPort
public interface BookRepository {

    Optional<Book> findAvailableBook(Book.Barcode inventoryNumber);

    Optional<Book> findOnHoldBook(Book.Barcode inventoryNumber);

    Book save(Book book);

    Optional<Book> findByBarcode(String barcode);
}
