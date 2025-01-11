package com.example.garageapp;

public class Customer {
    private int id;
    private String name;
    private String service_name;


    public int getOrderId() {
        return orderId;
    }

    private int orderId;

    public Customer(int id,String name, String service_name) {
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
