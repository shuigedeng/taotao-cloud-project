package com.taotao.cloud.web.output.builder;

import com.taotao.cloud.web.docx4j.output.utils.FileUtil;

import java.io.File;

/**
 * 用于测试excel导出的实体
 */
public class Person {
    private String name;
    private Integer age;
    private String sex;
    private String picture;

    public File picture() {
        return new File(FileUtil.rootPath(this.getClass(), this.picture));
    }
	public Person(){}
	public Person(String name, Integer age, String sex, String picture) {
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.picture = picture;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
}
