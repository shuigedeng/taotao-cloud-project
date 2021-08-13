package com.taotao.cloud.java.javaee.s2.c5_redis.redis_connect.entity;


import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private Integer id;

    private String name;

    private Date birthday;
	public User(){}
	public User(Integer id, String name, Date birthday) {
		this.id = id;
		this.name = name;
		this.birthday = birthday;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
}
