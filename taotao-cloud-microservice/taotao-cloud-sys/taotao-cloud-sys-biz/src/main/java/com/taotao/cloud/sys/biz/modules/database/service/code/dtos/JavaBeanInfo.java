package com.taotao.cloud.sys.biz.modules.database.service.code.dtos;

import com.sanri.tools.modules.database.service.meta.dtos.Column;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
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

    @Data
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

    }

    public void addImport(String importClass){
        imports.add(importClass);
    }

    public void addField(BeanField beanField){
        fields.add(beanField);
    }
}
