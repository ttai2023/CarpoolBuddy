package com.example.carpoolbuddy;

import java.util.ArrayList;

public class Bicycle extends Vehicle
{
    String bicycleType;
    int weight;
    int weightCapacity;

    public Bicycle()
    {

    }
    public Bicycle(String owner, String model, int capacity, ArrayList<String> riderUIDs, boolean open, String vehicleType, double basePrice, String bicycleType, int weight, int weightCapacity)
    {
        super(owner, model, capacity, riderUIDs, open, vehicleType, basePrice);
        this.bicycleType = bicycleType;
        this.weight = weight;
        this.weightCapacity = weightCapacity;
    }

    public String getBicycleType()
    {
        return bicycleType;
    }

    public void setBicycleType(String bicycleType)
    {
        this.bicycleType = bicycleType;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(int weight)
    {
        this.weight = weight;
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
        return "Bicycle{" +
                "bicycleType='" + bicycleType + '\'' +
                ", weight=" + weight +
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
