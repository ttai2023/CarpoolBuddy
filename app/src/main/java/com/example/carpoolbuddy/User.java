package com.example.carpoolbuddy;

import java.util.ArrayList;
import java.util.UUID;

public class User
{
    String uid;
    String name;
    String email;
    String userType;
    double priceMultiplier;
    ArrayList<String> ownedVehicles;
    double balance;

    public User()
    {

    }

    public User(String uid, String name, String email, String userType, double priceMultiplier, ArrayList<String> ownedVehicles)
    {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.priceMultiplier = priceMultiplier;
        this.ownedVehicles = ownedVehicles;
        this.balance = 2000;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUserType()
    {
        return userType;
    }

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public double getPriceMultiplier()
    {
        return priceMultiplier;
    }

    public void setPriceMultiplier(double priceMultiplier)
    {
        this.priceMultiplier = priceMultiplier;
    }

    public ArrayList<String> getOwnedVehicles()
    {
        return ownedVehicles;
    }

    public void setOwnedVehicles(ArrayList<String> ownedVehicles)
    {
        this.ownedVehicles = ownedVehicles;
    }

    public double getBalance()
    {
        return balance;
    }

    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userType='" + userType + '\'' +
                ", priceMultiplier=" + priceMultiplier +
                ", ownedVehicles=" + ownedVehicles +
                '}';
    }
}
