package com.taotao.cloud.java.javaweb.p12_myshop.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 对应数据库订单项
 */
public class Item implements Serializable {

    private static  final long serialVersionUID = 1L;

    private int iid;
    private String oid;
    private int pid;
    private Product product;
    private BigDecimal icount;
    private int inum;


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public BigDecimal getIcount() {
        return icount;
    }

    public void setIcount(BigDecimal icount) {
        this.icount = icount;
    }

    public int getInum() {
        return inum;
    }

    public void setInum(int inum) {
        this.inum = inum;
    }

    @Override
    public String toString() {
        return "Item{" +
                "iid=" + iid +
                ", oid='" + oid + '\'' +
                ", pid=" + pid +
                ", icount=" + icount +
                ", inum=" + inum +
                '}';
    }
}
