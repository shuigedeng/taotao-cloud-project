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

package com.taotao.cloud.modulith.borrow.infrastructure.in.rest;

import com.taotao.cloud.modulith.borrow.application.CirculationDesk;
import com.taotao.cloud.modulith.borrow.application.HoldDto;
import com.taotao.cloud.modulith.borrow.domain.Book;
import com.taotao.cloud.modulith.borrow.domain.Hold;
import com.taotao.cloud.modulith.borrow.domain.Patron.PatronId;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jmolecules.architecture.hexagonal.PrimaryAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@PrimaryAdapter
@RestController
@RequiredArgsConstructor
public class CirculationDeskController {

    private final CirculationDesk circulationDesk;

    @PostMapping("/borrow/holds")
    ResponseEntity<HoldDto> holdBook(@RequestBody HoldRequest request) {
        Hold.PlaceHold command =
                new Hold.PlaceHold(
                        new Book.Barcode(request.barcode()),
                        LocalDate.now(),
                        new PatronId(request.patronId()));
        HoldDto holdDto = circulationDesk.placeHold(command);
        return ResponseEntity.ok(holdDto);
    }

    @GetMapping("/borrow/holds/{id}")
    ResponseEntity<HoldDto> viewSingleHold(@PathVariable("id") UUID holdId) {
        return circulationDesk
                .locate(holdId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    record HoldRequest(String barcode, UUID patronId) {}
}
