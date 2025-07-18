package com.taotao.cloud.cache.simple;

public class User {
    private String name;  // 无注解，使用默认反序列化
    
    @SpecialField  // 有注解，使用自定义处理
    private String email;
    
    private int age;
    private Address address;  // 嵌套对象

    // 构造函数、getter和setter
    public User() {}
    
    public User(String name, String email, int age, Address address) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.address = address;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    @Override
    public String toString() {
        return "User{name='" + name + "', email='" + email + "', age=" + age + 
               ", address=" + address + "}";
    }
}
