package com.example.garageapp;

public class Customer {
    private int id;
    private String name;
    private String service_name;
    private int serviceId ;
    private String state ;
    private String statesDate ;
    private String orderDate ;
    private double totalAmount;

    public int getServiceId() {
        return serviceId;
    }

    public String getState() {
        return state;
    }

    public String getStatesDate() {
        return statesDate;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    private int orderId;

    public Customer(int id, String name, String service_name,int orderId, int serviceId, String state, String statesDate, String orderDate, double totalAmount) {
        this.id = id;
        this.name = name;
        this.service_name = service_name;
        this.serviceId = serviceId;
        this.state = state;
        this.statesDate = statesDate;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderId = orderId;
    }

    public Customer(int id, String name, String service_name) {
        this.id=id;
        this.name = name;
        this.service_name = service_name;
        this.orderId=getOrderId();
    }
    public  int getId(){
        return  id;
    }

    public String getName() {
        return name;
    }

    public String getService_name() {
        return service_name;
    }
}