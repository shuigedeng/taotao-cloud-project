package com.taotao.cloud.stock.api.common.domain;

import java.io.Serializable;

/**
 * Entity interface
 */
public interface Entity<T> extends Serializable {

    /**
     * Entities compare by identity, not by attributes.
     *
     * @param other The other entity.
     * @return true if the identities are the same, regardless of other attributes.
     */
    boolean sameIdentityAs(T other);
}
