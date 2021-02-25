package com.taotao.cloud.idea.plugin.toolset.model;

import cn.hutool.core.date.DateTime;

import java.util.Date;

public enum TimeUnit {
    Millisecond("毫秒") {
        @Override
        public String getLongValue(DateTime dateTime) {
            return String.valueOf(dateTime.getTime());
        }

        @Override
        public Date getDate(Long longValue) {
            return new Date(longValue);
        }
    },
    Second("秒") {
        @Override
        public String getLongValue(DateTime dateTime) {
            return String.valueOf(dateTime.getTime() / 1000);
        }

        @Override
        public Date getDate(Long longValue) {
            return new Date(longValue * 1000);
        }
    };

    private String label;

    TimeUnit(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public abstract String getLongValue(DateTime dateTime);

    public abstract Date getDate(Long longValue);
}
