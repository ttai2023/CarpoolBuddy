package com.example.carpoolbuddy;

import java.util.ArrayList;

public class Helicopter extends Vehicle
{
    int maxAltitude;
    int maxAirSpeed;

    public Helicopter()
    {

    }

    public Helicopter(String owner, String model, int capacity, ArrayList<String> riderUIDs, boolean open, String vehicleType, double basePrice, int maxAltitude, int maxAirSpeed)
    {
        super(owner, model, capacity, riderUIDs, open, vehicleType, basePrice);
        this.maxAltitude = maxAltitude;
        this.maxAirSpeed = maxAirSpeed;
    }

    public int getMaxAltitude()
    {
        return maxAltitude;
    }

    public void setMaxAltitude(int maxAltitude)
    {
        this.maxAltitude = maxAltitude;
    }

    public int getMaxAirSpeed()
    {
        return maxAirSpeed;
    }

    public void setMaxAirSpeed(int maxAirSpeed)
    {
        this.maxAirSpeed = maxAirSpeed;
    }

    @Override
    public String toString()
    {
        return "Helicopter{" +
                "maxAltitude=" + maxAltitude +
                ", maxAirSpeed=" + maxAirSpeed +
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
