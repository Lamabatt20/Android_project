package com.example.garageapp;

public class Employee {
    private int employeeId;
    private String employeeName;
    private String phoneNumber;
    private int authenticationCodeId;
    private String email;
    private String photo;

    // Constructor
    public Employee(int employeeId, String employeeName, String phoneNumber, int authenticationCodeId, String email, String photo) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.phoneNumber = phoneNumber;
        this.authenticationCodeId = authenticationCodeId;
        this.email = email;
        this.photo = photo;
    }

    public Employee(int employeeId,String employeeName){
        this.employeeName = employeeName;
        this.employeeId = employeeId;
    }


    // Getters
    public int getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getAuthenticationCodeId() {
        return authenticationCodeId;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    // Setters
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAuthenticationCodeId(int authenticationCodeId) {
        this.authenticationCodeId = authenticationCodeId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    // toString Method for Displaying Employee Information
    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", authenticationCodeId=" + authenticationCodeId +
                ", email='" + email + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
