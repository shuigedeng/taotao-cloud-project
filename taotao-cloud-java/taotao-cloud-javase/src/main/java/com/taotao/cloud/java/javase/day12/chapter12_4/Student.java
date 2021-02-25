package com.taotao.cloud.java.javase.day12.chapter12_4;

public class Student implements Comparable<Student>{
	private String name;
	private int stuNo;
	
	public Student() {
		// TODO Auto-generated constructor stub
	}

	public Student(String name, int stuNo) {
		super();
		this.name = name;
		this.stuNo = stuNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStuNo() {
		return stuNo;
	}

	public void setStuNo(int stuNo) {
		this.stuNo = stuNo;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + stuNo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (stuNo != other.stuNo)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", stuNo=" + stuNo + "]";
	}

	@Override
	public int compareTo(Student o) {
		int n2=this.stuNo-o.getStuNo();
		return n2;
	}
	
	
	
	
}
