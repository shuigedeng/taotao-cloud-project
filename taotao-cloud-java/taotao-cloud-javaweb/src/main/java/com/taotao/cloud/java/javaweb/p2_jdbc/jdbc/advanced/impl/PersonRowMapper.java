package com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.advanced.impl;


import com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.advanced.RowMapper;
import com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.entity.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class PersonRowMapper implements RowMapper<Person> {
	@Override
	public Person getRow(ResultSet resultSet) {
		Person person = null;
		try {
			int pid = resultSet.getInt("id");
			String name = resultSet.getString("name");
			int age = resultSet.getInt("age");
			Date borndate = resultSet.getDate("borndate");
			String email = resultSet.getString("email");
			String address = resultSet.getString("address");
			person = new Person(pid, name, age, borndate, email, address);
			return person;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
