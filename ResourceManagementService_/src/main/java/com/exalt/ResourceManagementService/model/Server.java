package com.exalt.ResourceManagementService.model;

import org.springframework.data.annotation.Id;

public class Server{

    @Id
    private int id;
    private int capacity = 0;// used space in server
    private boolean active = false;
    //final int SIZE = 100;


    public Server() {
    }

    public Server(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


}
