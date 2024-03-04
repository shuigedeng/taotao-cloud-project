package com.taotao.cloud.modulith.borrow.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Patron {

    private final PatronId id;

    @Enumerated(EnumType.STRING)
    private Membership status;

    private Patron(Membership status) {
        this.id = new PatronId(UUID.randomUUID());
        this.status = status;
    }

    public static Patron of(Membership status) {
        return new Patron(status);
    }

    public void deactivate() {
        this.status = Membership.INACTIVE;
    }

    public record PatronId(UUID id) {
    }

    public enum Membership {
        ACTIVE, INACTIVE
    }
}
