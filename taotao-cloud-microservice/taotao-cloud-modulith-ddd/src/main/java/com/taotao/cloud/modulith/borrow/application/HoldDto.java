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

package com.taotao.cloud.modulith.borrow.application;

import com.taotao.cloud.modulith.borrow.domain.Hold;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class HoldDto {

    private final String id;
    private final String bookBarcode;
    private final String patronId;
    private final LocalDate dateOfHold;

    private HoldDto(String id, String bookBarcode, String patronId, LocalDate dateOfHold) {
        this.id = id;
        this.bookBarcode = bookBarcode;
        this.patronId = patronId;
        this.dateOfHold = dateOfHold;
    }

    public static HoldDto from(Hold hold) {
        return new HoldDto(
                hold.getId().id().toString(),
                hold.getOnBook().barcode(),
                hold.getHeldBy().id().toString(),
                hold.getDateOfHold());
    }
}
