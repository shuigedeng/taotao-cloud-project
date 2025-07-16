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

package com.taotao.cloud.tidb.model;

import java.util.Objects;

/**
 * TestOrder
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2021/03/16 16:59
 */
public class TidbUser {

    private String id;

    private Integer sales;

    private Integer month;

    @Override
    public String toString() {
        return "TestOrder{" + "id='" + id + '\'' + ", sales=" + sales + ", month=" + month + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TidbUser user = (TidbUser) o;
        return Objects.equals(id, user.id)
                && Objects.equals(sales, user.sales)
                && Objects.equals(month, user.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sales, month);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
}
