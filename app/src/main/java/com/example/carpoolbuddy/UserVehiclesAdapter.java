package com.example.carpoolbuddy;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserVehiclesAdapter extends RecyclerView.Adapter<UserVehiclesViewHolder>
{
    ArrayList<Vehicle> vehicles;
    Context context;
    OnItemClickListener userVehicleListener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener userListener)
    {
        userVehicleListener = userListener;
    }

    public UserVehiclesAdapter(ArrayList<Vehicle> userVehicles, Context context)
    {
        vehicles = userVehicles;
        this.context = context;
    }

    @NonNull
    @Override
    public UserVehiclesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
         View vehiclesView = LayoutInflater.from(this.context).inflate(R.layout.user_vehicles_view, parent, false);
         UserVehiclesViewHolder holder = new UserVehiclesViewHolder(vehiclesView, userVehicleListener);
         return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserVehiclesViewHolder holder, int position)
    {
        Vehicle currVehicle = vehicles.get(position);
        holder.vehicleNameText.setText(currVehicle.model);
        holder.capacityText.setText("Capacity: " + String.valueOf(currVehicle.capacity));
        //show open/close depending on status
        if(currVehicle.isOpen())
        {
            holder.statusText.setText("Status: Open");
        }
        else
        {
            holder.statusText.setText("Status: Closed");
        }

        //set drawable according to the type of vehicle
        Drawable bicycle = ContextCompat.getDrawable(context, R.drawable.ic_bicycle);
        Drawable car = ContextCompat.getDrawable(context, R.drawable.ic_car);
        Drawable helicopter = ContextCompat.getDrawable(context, R.drawable.ic_helicopter);
        Drawable segway = ContextCompat.getDrawable(context, R.drawable.ic_segway);

        if(currVehicle.getVehicleType().equals("Bicycle"))
        {
            holder.optionView.setImageDrawable(bicycle);
        }
        else if (currVehicle.getVehicleType().equals("Car"))
        {
            holder.optionView.setImageDrawable(car);
        }
        else if(currVehicle.getVehicleType().equals("Helicopter"))
        {
            holder.optionView.setImageDrawable(helicopter);
        }
        else
        {
            holder.optionView.setImageDrawable(segway);
        }
    }

    @Override
    public int getItemCount()
    {
        return vehicles.size();
    }
}
