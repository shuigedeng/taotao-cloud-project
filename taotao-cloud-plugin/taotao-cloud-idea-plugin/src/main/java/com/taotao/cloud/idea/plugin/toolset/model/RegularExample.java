package com.taotao.cloud.idea.plugin.toolset.model;

public class RegularExample {
    private String name;
    private String regular;

    public RegularExample(String name, String regular) {
        this.name = name;
        this.regular = regular;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }
}
