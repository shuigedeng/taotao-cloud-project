package com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.view;


import com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.dao.PersonDao;
import com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.dao.impl.PersonDaoImpl;
import com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.entity.Person;

public class TestPerson {
	public static void main(String[] args) {
		PersonDao personDao = new PersonDaoImpl();
//        Person person = new Person("Gavin",19, DateUtils.strToUtil("1998-09-09"),"Gavin@163.com","北京市昌平区");
////        int result = personDao.insert(person);
////        System.out.println(result);
		Person person = personDao.select(6);
		System.out.println(person);
	}
}
