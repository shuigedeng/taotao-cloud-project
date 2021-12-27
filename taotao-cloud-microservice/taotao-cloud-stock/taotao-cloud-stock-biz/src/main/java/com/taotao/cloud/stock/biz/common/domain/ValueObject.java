package com.taotao.cloud.stock.biz.common.domain;

import java.io.Serializable;

/**
 * ValueObject interface
 *
 * @author haoxin
 * @date 2021-02-01
 **/
public interface ValueObject<T> extends Serializable {

    /**
     * Value objects compare by the values of their attributes, they don't have an identity.
     *
     * @param other The other value object.
     * @return <code>true</code> if the given value object's and this value object's attributes are the same.
     */
    boolean sameValueAs(T other);
}
