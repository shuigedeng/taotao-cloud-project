package com.taotao.cloud.sys.biz.tools.database.service.rename;

import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.Column;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaBeanInfo {
    private String className;
    private String lowerClassName;
    private Set<String> imports = new HashSet<>();
    private List<BeanField> fields = new ArrayList<>();

    public JavaBeanInfo() {
    }

    public JavaBeanInfo(String className) {
        this.className = className;
    }

    public static class BeanField{
        private String typeName;
        private String fieldName;
        private String comment;
        private boolean key;
        private Column column;
        private String capitalName;

        public BeanField() {
        }

        public BeanField(Column column,String typeName, String fieldName, String comment, boolean key) {
            this.column = column;
            this.typeName = typeName;
            this.fieldName = fieldName;
            this.comment = comment;
            this.key = key;
            this.capitalName = StringUtils.capitalize(fieldName);
        }

	    public String getTypeName() {
		    return typeName;
	    }

	    public void setTypeName(String typeName) {
		    this.typeName = typeName;
	    }

	    public String getFieldName() {
		    return fieldName;
	    }

	    public void setFieldName(String fieldName) {
		    this.fieldName = fieldName;
	    }

	    public String getComment() {
		    return comment;
	    }

	    public void setComment(String comment) {
		    this.comment = comment;
	    }

	    public boolean isKey() {
		    return key;
	    }

	    public void setKey(boolean key) {
		    this.key = key;
	    }

	    public Column getColumn() {
		    return column;
	    }

	    public void setColumn(Column column) {
		    this.column = column;
	    }

	    public String getCapitalName() {
		    return capitalName;
	    }

	    public void setCapitalName(String capitalName) {
		    this.capitalName = capitalName;
	    }
    }

    public void addImport(String importClass){
        imports.add(importClass);
    }

    public void addField(BeanField beanField){
        fields.add(beanField);
    }

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getLowerClassName() {
		return lowerClassName;
	}

	public void setLowerClassName(String lowerClassName) {
		this.lowerClassName = lowerClassName;
	}

	public Set<String> getImports() {
		return imports;
	}

	public void setImports(Set<String> imports) {
		this.imports = imports;
	}

	public List<BeanField> getFields() {
		return fields;
	}

	public void setFields(
		List<BeanField> fields) {
		this.fields = fields;
	}
}
