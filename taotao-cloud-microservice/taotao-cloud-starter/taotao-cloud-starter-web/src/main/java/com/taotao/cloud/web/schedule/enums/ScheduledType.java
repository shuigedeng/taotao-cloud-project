package com.taotao.cloud.web.schedule.enums;

/**
 * ScheduledType
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 17:04:21
 */
public enum ScheduledType {
    CRON(1, "cron"),
    FIXED_DELAY(2, "fixedDelay"),
    FIXED_RATE(3, "fixedRate");

    private int key;

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    ScheduledType(int key, String value) {
        this.key = key;
        this.value = value;
    }

    ScheduledType() {
    }

    public static String getValue(Integer key) {
        if (key == null) {
            return null;
        }
        ScheduledType[] enums = values();
        for (ScheduledType enu : enums) {
            if (enu.getKey() == key) {
                return enu.getValue();
            }
        }
        return null;
    }
}
