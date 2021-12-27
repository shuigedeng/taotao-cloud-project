package com.taotao.cloud.stock.biz.common.domain;

import java.io.Serializable;

/**
 * Aggregate interface
 *
 * @author haoxin
 * @date 2021-02-01
 **/
public interface Aggregate<T> extends Serializable {

    /**
     * Aggregate compare by identity, not by attributes.
     *
     * @param other The other Aggregate.
     * @return true if the identities are the same, regardless of other attributes.
     */
    boolean sameIdentityAs(T other);
}
