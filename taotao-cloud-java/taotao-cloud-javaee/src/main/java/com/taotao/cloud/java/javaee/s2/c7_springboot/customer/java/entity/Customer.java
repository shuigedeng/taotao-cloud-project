package com.taotao.cloud.java.javaee.s2.c7_springboot.customer.java.entity;


import java.io.Serializable;

public class Customer implements Serializable {

    /**
     * 主键
     *
     * isNullAble:0
     */
    private Integer id;

    /**
     * 公司名
     * isNullAble:1
     */
    private String username;

    /**
     *
     * isNullAble:1
     */
    private String password;

    /**
     *
     * isNullAble:1
     */
    private String nickname;

    /**
     * 金钱
     * isNullAble:1
     */
    private Long money;

    /**
     * 地址
     * isNullAble:1
     */
    private String address;

    /**
     * 状态
     * isNullAble:1
     */
    private Integer state;
}
