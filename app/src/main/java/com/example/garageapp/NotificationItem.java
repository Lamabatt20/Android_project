package com.example.garageapp;

public class NotificationItem {
    private int orderId;
    private String orderDate;
    private String description;
    private String state;
    private int carId;
    private String model;
    private String carName;

    // Constructor
    public NotificationItem(int orderId, String orderDate, String description, String state, int carId, String model, String carName) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.description = description;
        this.state = state;
        this.carId = carId;
        this.model = model;
        this.carName = carName;
    }

    // Getters
    public int getOrderId() { return orderId; }
    public String getOrderDate() { return orderDate; }
    public String getDescription() { return description; }
    public String getState() { return state; }
    public int getCarId() { return carId; }
    public String getModel() { return model; }

    public String getCarName() {
        return carName;
    }
}

