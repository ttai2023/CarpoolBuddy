package com.example.carpoolbuddy;

import java.util.ArrayList;

public class Segway extends Vehicle
{
    int range;
    int weightCapacity;

    public Segway()
    {

    }

    public Segway(String owner, String model, int capacity, ArrayList<String> riderUIDs, boolean open, String vehicleType, double basePrice, int range, int weightCapacity)
    {
        super(owner, model, capacity, riderUIDs, open, vehicleType, basePrice);
        this.range = range;
        this.weightCapacity = weightCapacity;
    }

    public int getRange()
    {
        return range;
    }

    public void setRange(int range)
    {
        this.range = range;
    }

    public int getWeightCapacity()
    {
        return weightCapacity;
    }

    public void setWeightCapacity(int weightCapacity)
    {
        this.weightCapacity = weightCapacity;
    }

    @Override
    public String toString()
    {
        return "Segway{" +
                "range=" + range +
                ", weightCapacity=" + weightCapacity +
                ", owner='" + owner + '\'' +
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
