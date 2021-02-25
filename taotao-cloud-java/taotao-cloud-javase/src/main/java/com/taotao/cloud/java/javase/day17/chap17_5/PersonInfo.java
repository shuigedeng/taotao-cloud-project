package com.taotao.cloud.java.javase.day17.chap17_5;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value= {ElementType.METHOD})
public @interface PersonInfo {
	String name();
	int age();
	String sex();
}
