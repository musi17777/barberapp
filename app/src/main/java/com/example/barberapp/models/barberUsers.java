package com.example.barberapp.models;

public class barberUsers {
    private String businessName;
    private String address;
    private String email;
    private String password;
    private String phone;
    public barberUsers(){

    }

    public barberUsers(String businessName, String address, String email, String password, String phone) {
        this.businessName = businessName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

