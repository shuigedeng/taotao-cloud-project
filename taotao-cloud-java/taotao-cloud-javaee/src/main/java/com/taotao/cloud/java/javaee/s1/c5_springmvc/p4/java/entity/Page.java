package com.taotao.cloud.java.javaee.s1.c5_springmvc.p4.java.entity;

public class Page {
    private Integer pageNum;
    private Integer pageSize=3;

    public Page(){}

    public Page(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
