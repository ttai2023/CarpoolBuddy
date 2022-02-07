package com.example.carpoolbuddy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleViewHolder>
{
    FirebaseFirestore firestore;
    ArrayList<Vehicle> vehicles;
    Context context;
    OnItemClickListener vehicleListener;
    String ownerName;
    String userType;

    public interface OnItemClickListener
    {
        void onItemClick(int position);
        void onAddClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener)
    {
        vehicleListener = listener;
    }

    public VehicleAdapter(ArrayList<Vehicle> vehiclesInfo, Context context)
    {
        vehicles = vehiclesInfo;
        this.context = context;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View vehiclesView = LayoutInflater.from(this.context).inflate(R.layout.vehicles_view, parent, false);
        VehicleViewHolder holder = new VehicleViewHolder(vehiclesView, vehicleListener);
        firestore = FirebaseFirestore.getInstance();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position)
    {
        //get the user's information to fill out the recycler view
        holder.vehicleNameText.setText(vehicles.get(position).model);
        firestore.collection("Users").document(vehicles.get(position).owner).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    try
                    {
                        DocumentSnapshot ds = task.getResult();
                        User user = ds.toObject(User.class);
                        ownerName = user.getName();
                        holder.ownerNameText.setText("Owner: " + ownerName);
                        userType = user.getUserType();
                        holder.priceText.setText("$" + String.valueOf((vehicles.get(position).basePrice) * user.getPriceMultiplier()));
                    }
                    catch(Exception error)
                    {
                        Log.d("ERROR", error.toString());
                    }
                }
            }
        });
        holder.capacityText.setText("Capacity: " + String.valueOf(vehicles.get(position).capacity));

    }

    @Override
    public int getItemCount()
    {
        return vehicles.size();
    }
}
