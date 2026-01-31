package com.taotao.cloud.idea.plugin.generateDBEntity;

/**
 * FieldInfo
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class FieldInfo {

    String name;
    String type;

    FieldInfo( String name, String type ) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType( String type ) {
        this.type = type;
    }
}
