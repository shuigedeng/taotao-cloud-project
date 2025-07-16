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

package com.taotao.cloud.modulith.catalog.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

@AggregateRoot
@Entity
@Getter
@NoArgsConstructor
@Table(name = "catalog_books", uniqueConstraints = @UniqueConstraint(columnNames = {"barcode"}))
public class CatalogBook {

    @Identity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    //    @Embedded
    //    private Barcode catalogNumber;

    private String isbn;

    // @Embedded
    //    @AttributeOverride(name = "name", column = @Column(name = "author"))
    //    private Author author;

    @Version private Long version;

    public CatalogBook(String title, Barcode catalogNumber, String isbn, Author author) {
        this.title = title;
        //        this.catalogNumber = catalogNumber;
        this.isbn = isbn;
        //        this.author = author;
    }

    public record Barcode(String barcode) {}

    public record Author(String name) {}
}
