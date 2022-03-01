package com.taotao.cloud.sys.biz.tools.redis.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class TreeKey {
    public static final char separatorChar = ':';
    private String key;
    private String name;
    private List<TreeKey> childs = new ArrayList<>();
    private boolean folder;

    public TreeKey() {
    }

    public TreeKey(String key,String name) {
        this.key = key;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TreeKey treeKey = (TreeKey) o;

        return treeKey.getKey().equals(getKey());
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    public void addChild(TreeKey treeKey){
        childs.add(treeKey);
    }


    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    @Override
    public String toString() {
        return "TreeKey{" +
                "name='" + name + '\'' +
                '}';
    }
}
