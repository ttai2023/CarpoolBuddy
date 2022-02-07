package com.example.carpoolbuddy;

import java.util.ArrayList;

public class Parent extends User
{
    ArrayList<String> childrenUIDs;

    public Parent()
    {

    }

    public Parent(String uid, String name, String email, String userType, double priceMultiplier, ArrayList<String> ownedVehicles, ArrayList<String> childrenUIDs)
    {
        super(uid, name, email, userType, priceMultiplier, ownedVehicles);
        this.childrenUIDs = childrenUIDs;
    }

    public ArrayList<String> getChildrenUIDs()
    {
        return childrenUIDs;
    }

    public void setChildrenUIDs(ArrayList<String> childrenUIDs)
    {
        this.childrenUIDs = childrenUIDs;
    }

    @Override
    public String toString()
    {
        return "Parent{" +
                "childrenUIDs=" + childrenUIDs +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userType='" + userType + '\'' +
                ", priceMultiplier=" + priceMultiplier +
                ", ownedVehicles=" + ownedVehicles +
                '}';
    }
}
