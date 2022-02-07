package com.example.carpoolbuddy;

import java.util.ArrayList;

public class Teacher extends User
{
    String inSchoolTitle;

    public Teacher()
    {

    }

    public Teacher(String uid, String name, String email, String userType, double priceMultiplier, ArrayList<String> ownedVehicles, String inSchoolTitle)
    {
        super(uid, name, email, userType, priceMultiplier, ownedVehicles);
        this.inSchoolTitle = inSchoolTitle;
    }

    public String getInSchoolTitle()
    {
        return inSchoolTitle;
    }

    public void setInSchoolTitle(String inSchoolTitle)
    {
        this.inSchoolTitle = inSchoolTitle;
    }

    @Override
    public String toString()
    {
        return "Teacher{" +
                "inSchoolTitle='" + inSchoolTitle + '\'' +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userType='" + userType + '\'' +
                ", priceMultiplier=" + priceMultiplier +
                ", ownedVehicles=" + ownedVehicles +
                '}';
    }

}
