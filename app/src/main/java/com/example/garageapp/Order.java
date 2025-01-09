package com.example.garageapp;

public class Order {
    private int orderId;
    private int serviceId;
    private int carId;
    private String state;
    private String serviceName;
    private String carName;
    private String orderDate;

    public Order(int orderId, int serviceId, int carId, String state, String serviceName, String carName, String orderDate) {
        this.orderId = orderId;
        this.serviceId = serviceId;
        this.carId = carId;
        this.state = state;
        this.serviceName = serviceName;
        this.carName = carName;
        this.orderDate = orderDate;
    }

    public String getServiceName() { return serviceName; }
    public String getCarName() { return carName; }
    public String getOrderDate() { return orderDate; }
    public String getState() { return state; }
}