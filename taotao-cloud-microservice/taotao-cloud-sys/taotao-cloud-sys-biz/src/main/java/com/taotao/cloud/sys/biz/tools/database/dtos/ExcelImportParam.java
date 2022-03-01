package com.taotao.cloud.sys.biz.tools.database.dtos;


import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class ExcelImportParam {
    @NotNull
    private String connName;
    @Valid
    private ActualTableName actualTableName;
    @PositiveOrZero
    private int startRow;
    @Valid
    private List<Mapping> mapping;

    public static class Mapping{
        private int index = -1;
        @NotNull
        private String columnName;
        private String random;
        private String constant;

	    public int getIndex() {
		    return index;
	    }

	    public void setIndex(int index) {
		    this.index = index;
	    }

	    public String getColumnName() {
		    return columnName;
	    }

	    public void setColumnName(String columnName) {
		    this.columnName = columnName;
	    }

	    public String getRandom() {
		    return random;
	    }

	    public void setRandom(String random) {
		    this.random = random;
	    }

	    public String getConstant() {
		    return constant;
	    }

	    public void setConstant(String constant) {
		    this.constant = constant;
	    }
    }

	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

	public ActualTableName getActualTableName() {
		return actualTableName;
	}

	public void setActualTableName(ActualTableName actualTableName) {
		this.actualTableName = actualTableName;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public List<Mapping> getMapping() {
		return mapping;
	}

	public void setMapping(
		List<Mapping> mapping) {
		this.mapping = mapping;
	}
}
