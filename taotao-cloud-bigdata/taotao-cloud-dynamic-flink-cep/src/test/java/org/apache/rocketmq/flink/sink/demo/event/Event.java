package org.apache.rocketmq.flink.sink.demo.event;

import javax.annotation.Nullable;
import java.util.Objects;

/** Exemplary event for usage in tests of CEP. */
public class Event {
    private final int id;
    private final String name;

    private final int productionId;
    private final int action;
    private final long eventTime;
    private final String eventArgs;

    public Event(int id, String name, int action, int productionId, long timestamp, @Nullable String eventArgs) {
        this.id = id;
        this.name = name;
        this.action = action;
        this.productionId = productionId;
        this.eventTime = timestamp;
        this.eventArgs = eventArgs;
    }

    public Event(int id, String name, int action, int productionId, long timestamp) {
        this(id, name, action, productionId, timestamp, null);
    }

    public static Event fromString(String eventStr) {
        String[] split = eventStr.split(",");
        return new Event(
                Integer.parseInt(split[0]),
                split[1],
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                Long.parseLong(split[4]),
                split[5]);
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

    public String getEventArgs() {
        return eventArgs;
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
