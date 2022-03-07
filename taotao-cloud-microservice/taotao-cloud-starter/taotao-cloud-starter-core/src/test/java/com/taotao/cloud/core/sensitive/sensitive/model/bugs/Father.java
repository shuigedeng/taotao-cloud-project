package com.taotao.cloud.core.sensitive.sensitive.model.bugs;


import com.taotao.cloud.core.sensitive.sensitive.annotation.SensitiveEntry;

/**
 * @author binbin.hou
 * @since 0.0.11
 */
public class Father {

    @SensitiveEntry
    private Child child;

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }
}
