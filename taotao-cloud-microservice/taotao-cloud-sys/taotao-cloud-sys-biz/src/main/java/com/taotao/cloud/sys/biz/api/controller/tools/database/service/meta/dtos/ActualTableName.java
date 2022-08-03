package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Data
public class ActualTableName {
    /**
     * 命名空间
     */
    @Valid
    private Namespace namespace;
    /**
     * 表名
     */
    @NotNull
    private String tableName;

    public ActualTableName() {
    }

    public ActualTableName(String catalog, String schema, String tableName) {
        this.namespace = new Namespace(catalog,schema);
        this.tableName = tableName;
    }

    public ActualTableName(Namespace namespace, @NotNull String tableName) {
        this.namespace = namespace;
        this.tableName = tableName;
    }

    @JsonIgnore
    public String getCatalog(){
        return namespace.getCatalog();
    }

    @JsonIgnore
    public String getSchema(){
        return namespace.getSchema();
    }

    public String getFullName() {
        return StringUtils.join(Arrays.asList(namespace.getCatalog(),namespace.getSchema(),tableName),'.');
    }
}
