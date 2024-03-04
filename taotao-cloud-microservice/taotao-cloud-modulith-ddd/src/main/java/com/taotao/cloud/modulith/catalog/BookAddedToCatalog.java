package com.taotao.cloud.modulith.catalog;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record BookAddedToCatalog(String title, String inventoryNumber,
                                 String isbn, String author) {
}
