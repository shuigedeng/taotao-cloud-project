package com.taotao.cloud.cache.other1;


public class User {
    private String name;
    
    @CustomProcess(processor = "uppercase")
    private String email;

	private Address address;


	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
