package com.example.garageapp;

public class Car {
    private int carId;
    private int customerId;
    private String licensePlate;
    private String model;
    private String make;
    private String cars_name;
    private String odometer;
    private String engineSpecification;
    private String transmission;
    private String company;
    private String photo;

    public Car(String name, String photo, int carid) {
        this.photo = photo;
        this.cars_name=name;
        this.carId=carid;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    private String year;

    public String getCars_name() {
        return cars_name;
    }



    public Car(String cars_name, String photo) {
        this.photo = photo;
        this.cars_name=cars_name;
    }

    public Car(int carId, int customerId, String licensePlate, String model, String make, String car_name, String odometer, String engineSpecification, String transmission, String company, String photo, String year) {
        this.carId = carId;
        this.customerId = customerId;
        this.licensePlate = licensePlate;
        this.model = model;
        this.make = make;
        this.cars_name = car_name;
        this.odometer = odometer;
        this.engineSpecification = engineSpecification;
        this.transmission = transmission;
        this.company = company;
        this.photo = photo;
        this.year = year;
    }

    public int getCarId() {
        return carId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getModel() {
        return model;
    }

    public String getMake() {
        return make;
    }

    public String getYear() {
        return year;
    }

    public String getOdometer() {
        return odometer;
    }

    public String getEngineSpecification() {
        return engineSpecification;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getCompany() {
        return company;
    }

    public String getPhoto() {
        return photo;
    }
}
