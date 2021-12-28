/*
 * Copyright (C) 2019-2020 the original author or authors.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.taotao.cloud.stock.api.common.util;

public enum ObjectType {

    UNKNOWN(1000),

    APPLICATION(1101),

    // keystone
    TENANT(1201),
    USER(1202),

    // customer
    CUSTOMER(1301),

    // store
    STORE(1401),
    STORE_EDITION(1402),
    STORE_ROLE(1403),
    STORE_STAFF(1404),
    STORE_MEMBER(1405);

    private final int code;

    ObjectType(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
