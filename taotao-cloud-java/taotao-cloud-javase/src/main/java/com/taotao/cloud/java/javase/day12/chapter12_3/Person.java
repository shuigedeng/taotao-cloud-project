package com.taotao.cloud.java.javase.day12.chapter12_3;
/**
 * 人类
 * @author wgy
 *
 */
public class Person implements Comparable<Person>{
	private String name;
	private int age;
	public Person() {
		// TODO Auto-generated constructor stub
	}
	public Person(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + "]";
	}
	@Override
	public int hashCode() {
		//(1)31是一个质数，减少散列冲突 (2)31提高执行效率    31*i=(i<<5)-i
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Person other = (Person) obj;
		if (age != other.age)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	//先按姓名比，然后再按年龄比
	@Override
	public int compareTo(Person o) {
		int n1=this.getName().compareTo(o.getName());
		int n2=this.age-o.getAge();
		
		return n1==0?n2:n1;
	}
	
//	@Override
//	public int hashCode() {
//		int n1=this.name.hashCode();
//		int n2=this.age;
//		
//		return n1+n2;
//		
//	}
//	
//	@Override
//	public boolean equals(Object obj) {
//		if(this==obj) {
//			return true;
//		}
//		if(obj==null) {
//			return false;
//		}
//		if(obj instanceof Person) {
//			Person p=(Person)obj;
//			
//			if(this.name.equals(p.getName())&&this.age==p.getAge()) {
//				return true;
//			}
//		}
//	
//		return false;
//	}
	
	
	
}
