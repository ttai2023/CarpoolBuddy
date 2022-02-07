package com.example.carpoolbuddy;

import java.util.ArrayList;

public class Alumni extends User
{
    String graduateYear;

    public Alumni()
    {

    }
    public Alumni(String uid, String name, String email, String userType, double priceMultiplier, ArrayList<String> ownedVehicles, String graduateYear)
    {
        super(uid, name, email, userType, priceMultiplier, ownedVehicles);
        this.graduateYear = graduateYear;
    }

    public String getGraduateYear()
    {
        return graduateYear;
    }

    public void setGraduateYear(String graduateYear)
    {
        this.graduateYear = graduateYear;
    }

    @Override
    public String toString()
    {
        return "Alumni{" +
                "graduateYear='" + graduateYear + '\'' +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userType='" + userType + '\'' +
                ", priceMultiplier=" + priceMultiplier +
                ", ownedVehicles=" + ownedVehicles +
                '}';
    }
}
