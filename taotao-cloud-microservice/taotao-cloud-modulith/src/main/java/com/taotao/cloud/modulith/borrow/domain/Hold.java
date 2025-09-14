/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.modulith.borrow.domain;

import com.taotao.cloud.modulith.borrow.domain.Patron.PatronId;
import java.time.LocalDate;
import java.util.UUID;
import java.util.function.UnaryOperator;
import lombok.Getter;
import org.jmolecules.ddd.types.Identifier;

@Getter
public class Hold {

    private final HoldId id;

    private final Book.Barcode onBook;

    private final PatronId heldBy;

    private final LocalDate dateOfHold;

    private Hold(PlaceHold placeHold) {
        this.id = new HoldId(UUID.randomUUID());
        this.onBook = placeHold.inventoryNumber();
        this.dateOfHold = placeHold.dateOfHold();
        this.heldBy = placeHold.patronId();
    }

    public static Hold placeHold(PlaceHold command) {
        return new Hold(command);
    }

    public Hold then(UnaryOperator<Hold> function) {
        return function.apply(this);
    }

    public record HoldId(UUID id) implements Identifier {}

    public record PlaceHold(
            Book.Barcode inventoryNumber, LocalDate dateOfHold, PatronId patronId) {}
}
