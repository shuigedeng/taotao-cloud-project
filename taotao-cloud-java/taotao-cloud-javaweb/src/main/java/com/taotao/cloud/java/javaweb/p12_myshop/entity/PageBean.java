package com.taotao.cloud.java.javaweb.p12_myshop.entity;

import java.util.List;

public class PageBean<T> {

    private List<T> list;
    private int currentPage;
    private int pageSize;
    private long totalCount; //总条数
    private int totalPage; //总页数

    public PageBean(List<T> list, int currentPage, int pageSize, long totalCount) {
        this.list = list;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        //13条数据  每页显示5   3
        return (int) Math.ceil(totalCount*1.0/pageSize);
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
