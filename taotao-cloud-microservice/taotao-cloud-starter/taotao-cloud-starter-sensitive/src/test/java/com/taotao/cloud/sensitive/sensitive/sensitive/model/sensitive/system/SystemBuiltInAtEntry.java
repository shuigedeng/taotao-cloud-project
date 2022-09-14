package com.taotao.cloud.sensitive.sensitive.sensitive.model.sensitive.system;


import com.taotao.cloud.sensitive.sensitive.sensitive.annotation.SensitiveEntry;

/**
 * 系统内置注解-对象
 */
public class SystemBuiltInAtEntry {

    @SensitiveEntry
    private SystemBuiltInAt entry;

    public SystemBuiltInAt getEntry() {
        return entry;
    }

    public void setEntry(SystemBuiltInAt entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return "SystemBuiltInAtEntry{" +
                "entry=" + entry +
                '}';
    }

}
