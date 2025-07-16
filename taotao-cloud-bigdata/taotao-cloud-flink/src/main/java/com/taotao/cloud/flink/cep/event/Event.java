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

package com.taotao.cloud.flink.cep.event;

import java.util.Objects;

/** Exemplary event for usage in tests of CEP. */
public class Event {
    private final int id;
    private final String name;

    private final int productionId;
    private final int action;
    private final long eventTime;

    public Event(int id, String name, int action, int productionId, long timestamp) {
        this.id = id;
        this.name = name;
        this.action = action;
        this.productionId = productionId;
        this.eventTime = timestamp;
    }

    public static Event fromString(String eventStr) {
        String[] split = eventStr.split(",");
        return new Event(
                Integer.parseInt(split[0]),
                split[1],
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                Long.parseLong(split[4]));
    }

    public long getEventTime() {
        return eventTime;
    }

    public double getAction() {
        return action;
    }

    public int getId() {
        return id;
    }

    public int getProductionId() {
        return productionId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Event("
                + id
                + ", "
                + name
                + ", "
                + action
                + ", "
                + productionId
                + ", "
                + eventTime
                + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Event) {
            Event other = (Event) obj;
            return name.equals(other.name)
                    && action == other.action
                    && productionId == other.productionId
                    && id == other.id;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, action, productionId, id);
    }
}
