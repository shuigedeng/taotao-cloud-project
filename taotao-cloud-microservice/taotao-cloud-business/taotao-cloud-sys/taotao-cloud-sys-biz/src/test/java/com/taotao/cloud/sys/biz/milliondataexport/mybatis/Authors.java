package com.taotao.cloud.sys.biz.milliondataexport.mybatis;

/**
 * Authors
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class Authors {

    private Integer id;
    private String firstName;

    private String lastName;

    private String email;

    private Date birthdate;

    private Date added;

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName == null ? null : firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName == null ? null : lastName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email == null ? null : email.trim();
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate( Date birthdate ) {
        this.birthdate = birthdate;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded( Date added ) {
        this.added = added;
    }

    @Override
    public String toString() {
        return this.id + "," + this.firstName + "," + this.lastName + "," + this.email + "," + this.birthdate + ","
                + this.added;
    }
}
