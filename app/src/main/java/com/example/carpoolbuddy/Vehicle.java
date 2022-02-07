package com.example.carpoolbuddy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Vehicle implements Serializable
{
    String owner;
    String model;
    int capacity;
    String vehicleID;
    ArrayList<String> riderUIDs;
    boolean open;
    String vehicleType;
    double basePrice;

    public Vehicle()
    {

    }

    public Vehicle(String owner, String model, int capacity, ArrayList<String> riderUIDs, boolean open, String vehicleType, double basePrice)
    {
        this.owner = owner;
        this.model = model;
        this.capacity = capacity;
        this.vehicleID = UUID.randomUUID().toString();
        this.riderUIDs = riderUIDs;
        this.open = open;
        this.vehicleType = vehicleType;
        this.basePrice = basePrice;
    }

    public String getOwner()
    {
        return owner;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public int getCapacity()
    {
        return capacity;
    }

    public void setCapacity(int capacity)
    {
        this.capacity = capacity;
    }

    public String getVehicleID()
    {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID)
    {
        this.vehicleID = vehicleID;
    }

    public ArrayList<String> getRiderUIDs() {
        return riderUIDs;
    }

    public void setRiderUIDs(ArrayList<String> riderUIDs)
    {
        this.riderUIDs = riderUIDs;
    }

    public boolean isOpen()
    {
        return open;
    }

    public void setOpen(boolean open)
    {
        this.open = open;
    }

    public String getVehicleType()
    {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType)
    {
        this.vehicleType = vehicleType;
    }

    public double getBasePrice()
    {
        return basePrice;
    }

    public void setBasePrice(double basePrice)
    {
        this.basePrice = basePrice;
    }

    @Override
    public String toString()
    {
        return "Vehicle{" +
                "owner='" + owner + '\'' +
                ", model='" + model + '\'' +
                ", capacity=" + capacity +
                ", vehicleID='" + vehicleID + '\'' +
                ", riderUIDs=" + riderUIDs +
                ", open=" + open +
                ", vehicleType='" + vehicleType + '\'' +
                ", basePrice=" + basePrice +
                '}';
    }
}
