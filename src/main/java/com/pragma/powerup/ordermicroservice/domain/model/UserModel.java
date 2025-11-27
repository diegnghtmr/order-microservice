package com.pragma.powerup.ordermicroservice.domain.model;

public class UserModel {
    private Long id;
    private String name;
    private String lastName;
    private String cellPhone;

    public UserModel() {
    }

    public UserModel(Long id, String name, String lastName, String cellPhone) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.cellPhone = cellPhone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }
}
