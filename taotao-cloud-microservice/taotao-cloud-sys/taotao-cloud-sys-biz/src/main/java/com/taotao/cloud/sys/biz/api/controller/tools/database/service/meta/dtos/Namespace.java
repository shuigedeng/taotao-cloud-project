package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString
public class Namespace {
    private String catalog;
    private String schema;

    public Namespace() {
    }

    public Namespace(String catalog, String schema) {
        this.catalog = catalog;
        this.schema = schema;
    }

    public String toNamespace(){
        return StringUtils.join(new String[]{catalog,schema},"@");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Namespace namespace = (Namespace) o;

        if (catalog != null ? !catalog.equalsIgnoreCase(namespace.catalog) : namespace.catalog != null) {
            return false;
        }
        if (schema != null ? !schema.equalsIgnoreCase(namespace.schema) : namespace.schema != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = catalog != null ? catalog.toUpperCase().hashCode() : 0;
        result = 31 * result + (schema != null ? schema.toUpperCase().hashCode() : 0);
        return result;
    }
}
