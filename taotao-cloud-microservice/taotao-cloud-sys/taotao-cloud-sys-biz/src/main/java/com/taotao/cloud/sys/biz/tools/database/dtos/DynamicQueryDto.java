package com.taotao.cloud.sys.biz.tools.database.dtos;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	    public String getColumnName() {
		    return columnName;
	    }

	    public void setColumnName(String columnName) {
		    this.columnName = columnName;
	    }

	    public int getDataType() {
		    return dataType;
	    }

	    public void setDataType(int dataType) {
		    this.dataType = dataType;
	    }

	    public String getTypeName() {
		    return typeName;
	    }

	    public void setTypeName(String typeName) {
		    this.typeName = typeName;
	    }
    }

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Header> getHeaders() {
		return headers;
	}

	public void setHeaders(
		List<Header> headers) {
		this.headers = headers;
	}

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}
}
