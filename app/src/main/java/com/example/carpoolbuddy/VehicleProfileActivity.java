package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This class provides methods that allow the user to
 * take a closer look at a vehicle's information and
 * allow them to either book/open/close the vehicle
 * depending on whether they are the owner and whether
 * the vehicle is currently opened or closed
 *
 * @author Terri Tai
 * @version 1.0
 */

public class VehicleProfileActivity extends AppCompatActivity
{
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Vehicle vehicleInfo;
    TextView modelName;
    TextView vehicleType;
    TextView capacityText;
    TextView priceText;
    String name;
    String type;
    String capacity;
    double price;
    double balance;
    Button option;
    ImageView vehicleView;

//    Setup UI layout, firebase instances and display info for textViews

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_profile);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        //obtain selected vehicle from userVehiclesFragment/vehicleInfoFragment
        vehicleInfo = UserVehiclesFragment.userVehicle;
        if(vehicleInfo == null)
        {
            vehicleInfo = VehiclesInfoFragment.vehicle;
        }
        modelName = this.findViewById(R.id.modelName);
        vehicleType = this.findViewById(R.id.vehicleType);
        capacityText = this.findViewById(R.id.capacityText);
        priceText = this.findViewById(R.id.priceText);
        vehicleView = this.findViewById(R.id.vehicleView);
        option = this.findViewById(R.id.optionButton);

        //if user is the owner of the vehicle, show open/close
        //if vehicle is opened, show close
        //if vehicle is closed, show open

        if(vehicleInfo.getOwner().equals(mUser.getUid()))
        {
            if(vehicleInfo.isOpen())
            {
                option.setText("Close");
            }
            else
            {
                option.setText("Open");
            }
        }

        setVehicleInfo();

        Drawable bicycle = getResources().getDrawable(R.drawable.ic_bicycle);
        Drawable car = getResources().getDrawable(R.drawable.ic_car);
        Drawable helicopter = getResources().getDrawable(R.drawable.ic_helicopter);
        Drawable segway = getResources().getDrawable(R.drawable.ic_segway);

        //set drawable depending on the type of vehicle
        if(vehicleInfo.getVehicleType().equals("Bicycle"))
        {
            vehicleView.setImageDrawable(bicycle);
        }
        else if (vehicleInfo.getVehicleType().equals("Car"))
        {
            vehicleView.setImageDrawable(car);
        }
        else if(vehicleInfo.getVehicleType().equals("Helicopter"))
        {
            vehicleView.setImageDrawable(helicopter);
        }
        else
        {
            vehicleView.setImageDrawable(segway);
        }
    }

//    Only shows “book ride” button if this user is not the owner of this vehicle.
//    Show the correct price in a label depending on the user’s role.
//    Teachers and students pay half price. Alumni and Parents pay full price.
//    Only shows “open/close” button if the user IS the owner of this vehicle.

    /**
     * This method gets user from Users collection using uid, sets display info
     * according to the Vehicle object obtained and
     * displays the vehicle price according to the type of user
     */

    void setVehicleInfo()
    {
        try
        {
            firestore.collection("Users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        DocumentSnapshot ds = task.getResult();
                        User user = ds.toObject(User.class);
                        name = vehicleInfo.getModel();
                        type = vehicleInfo.getVehicleType();
                        capacity = String.valueOf(vehicleInfo.getCapacity());
                        price = vehicleInfo.getBasePrice();
                        balance = user.getBalance();
                        modelName.setText("Model: " + name);
                        vehicleType.setText("Vehicle Type: " + type);
                        capacityText.setText("Capacity: " + capacity);
                        priceText.setText("$" + String.valueOf(price * user.getPriceMultiplier()));
                    }
                }
            });
        }
        catch(Exception error)
        {
            Log.d("ERROR", error.toString());
            Toast fail = Toast.makeText(this, "Cannot find vehicle.", Toast.LENGTH_LONG);
            fail.show();
        }
    }

    /**
     * This method sets the button's function depending on the text on the button
     * @param view from user interface
     */

    public void setUpButton(View v)
    {
        if(option.getText().equals("Book Ride"))
        {
            bookRide();
        }
        //if button shows "Close," set vehicle's open field to false in firestore
        else if(option.getText().equals("Close"))
        {
            vehicleInfo.setOpen(false);
            DocumentReference docRef = firestore.collection("Vehicles").document(vehicleInfo.getVehicleID());
            docRef.update("open", false);
            Toast success = Toast.makeText(this, "You have closed your vehicle. Please open to accept new bookings.", Toast.LENGTH_LONG);
            success.show();

        }
        //if button shows "Open," set vehicle's open field to true in firestore
        else
        {
            vehicleInfo.setOpen(true);
            DocumentReference docRef = firestore.collection("Vehicles").document(vehicleInfo.getVehicleID());
            docRef.update("open", true);
            Toast success = Toast.makeText(this, "Vehicle is now accepting bookings.", Toast.LENGTH_LONG);
            success.show();
        }

    }

//    Reduce current capacity for this vehicle, in the database by 1.

    /**
     * This method deducts 1 from capacity and adds user's Uid to vehicle's riderUids,
     * however, if capacity is 0, the vehicle will not be available for booking
     */

    void bookRide()
    {
        if(vehicleInfo.getCapacity() != 0)
        {
            String ID = vehicleInfo.getVehicleID();
            int updatedCapacity = vehicleInfo.getCapacity() - 1;
            ArrayList<String> updatedRiders = vehicleInfo.getRiderUIDs();
            if (updatedRiders.contains(mUser.getUid()))
            {
                Toast fail = Toast.makeText(this, "You have already booked this vehicle. Please await your vehicle to arrive.", Toast.LENGTH_LONG);
                fail.show();
            }
            else
            {
                updatedRiders.add(mUser.getUid());
                DocumentReference docRef = firestore.collection("Vehicles").document(ID);
                docRef.update("capacity", updatedCapacity);
                docRef.update("riderUIDs", updatedRiders);
                Toast success = Toast.makeText(this, "Successfully booked vehicle. Please await your vehicle to arrive.", Toast.LENGTH_LONG);
                success.show();
            }
        }
        else
        {
            Toast fail = Toast.makeText(this, "This vehicle is full. Waiting for owner to close.", Toast.LENGTH_LONG);
            fail.show();
        }
        double updatedBalance = balance - price;
        DocumentReference docRef = firestore.collection("Users").document(mUser.getUid());
        docRef.update("balance", updatedBalance);

    }

    /**
     * This method navigates the user to UserActivity if back button is pressed
     */
    public void onBackPressed()
    {
        vehicleInfo = null;
        Intent intent = new Intent(this, UserActivity.class);
        finish();
        startActivity(intent);
    }
}