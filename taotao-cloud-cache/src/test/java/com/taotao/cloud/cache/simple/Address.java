package com.taotao.cloud.cache.simple;

public class Address {
    private String street;
    
    @SpecialField  // 有注解，使用自定义处理
    private String city;
    
    private String zipCode;

    // 构造函数、getter和setter
    public Address() {}
    
    public Address(String street, String city, String zipCode) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    // Getters and Setters
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    @Override
    public String toString() {
        return "Address{street='" + street + "', city='" + city + "', zipCode='" + zipCode + "'}";
    }
}
