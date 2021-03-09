package com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatispage.java.entity;

public class Page {
    private Integer pageNum=1;
    private Integer pageSize=2;

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
