package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos;

import com.taotao.cloud.sys.biz.api.controller.tools.core.utils.Version;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClientInfo {
    private List<Property> properties = new ArrayList<>();
    private Version databaseVersion;
    private String databaseProductName;
    private String databaseProductVersion;
    private int defaultTransactionIsolation;
    private String driverName;
    private Version driverVersion;
    private Version jdbcVersion;
    private String keywords;

    @Data
    public static final class Property{
        private String name;
        private int maxLen;
        private String defaultValue;
        private String description;

        public Property() {
        }

        public Property(String name, int maxLen, String defaultValue, String description) {
            this.name = name;
            this.maxLen = maxLen;
            this.defaultValue = defaultValue;
            this.description = description;
        }
    }
}
