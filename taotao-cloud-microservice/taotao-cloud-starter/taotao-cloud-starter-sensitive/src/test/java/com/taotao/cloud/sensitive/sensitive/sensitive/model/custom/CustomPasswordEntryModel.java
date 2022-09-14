package com.taotao.cloud.sensitive.sensitive.sensitive.model.custom;


import com.taotao.cloud.sensitive.sensitive.sensitive.annotation.SensitiveEntry;

public class CustomPasswordEntryModel {

    @SensitiveEntry
    private CustomPasswordModel entry;

    public CustomPasswordModel getEntry() {
        return entry;
    }

    public void setEntry(CustomPasswordModel entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return "CustomPasswordEntryModel{" +
                "entry=" + entry +
                '}';
    }

}
