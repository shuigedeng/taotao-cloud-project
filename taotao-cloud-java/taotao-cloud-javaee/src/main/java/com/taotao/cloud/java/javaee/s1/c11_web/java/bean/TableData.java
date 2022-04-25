package com.taotao.cloud.java.javaee.s1.c11_web.java.bean;


import java.util.List;

/**
 * @author shuigedeng
 */
public class TableData<T> {
    private int code=0;
    private String msg;
    private long count;
    private List<T> data;

    public TableData() {
    }

    public TableData(long count, List<T> data) {
        this.count = count;
        this.data = data;
    }
}
