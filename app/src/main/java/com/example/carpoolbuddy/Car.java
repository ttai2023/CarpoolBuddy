package com.example.carpoolbuddy;

import java.util.ArrayList;

public class Car extends Vehicle
{
    int range;

    public Car()
    {

    }

    public Car(String owner, String model, int capacity, ArrayList<String> riderUIDs, boolean open, String vehicleType, double basePrice, int range)
    {
        super(owner, model, capacity, riderUIDs, open, vehicleType, basePrice);
        this.range = range;
    }

    public int getRange()
    {
        return range;
    }

    public void setRange(int range)
    {
        this.range = range;
    }
}
