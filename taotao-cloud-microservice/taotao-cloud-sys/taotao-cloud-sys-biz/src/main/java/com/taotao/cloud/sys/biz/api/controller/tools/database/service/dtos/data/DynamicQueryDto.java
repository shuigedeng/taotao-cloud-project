package com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class DynamicQueryDto {
    private String sql;
    private List<Header> headers = new ArrayList<>();
    private List<Map<String,Object>> rows = new ArrayList<>();

    public DynamicQueryDto() {
    }

    public DynamicQueryDto(String sql) {
        this.sql = sql;
    }

    public void addRow(Map<String,Object> row){
        rows.add(row);
    }

    public void addHeader(Header header) {
        headers.add(header);
    }

    @Data
    public static class Header{
        private String columnName;
        private int dataType;
        private String typeName;

        public Header() {
        }

        public Header(String columnName, int dataType, String typeName) {
            this.columnName = columnName;
            this.dataType = dataType;
            this.typeName = typeName;
        }
    }
}
