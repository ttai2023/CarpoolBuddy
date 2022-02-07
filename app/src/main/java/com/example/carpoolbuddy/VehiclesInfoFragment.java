package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This class provides methods that allow the user to
 * view the available vehicles in the database, including
 * the model, owner, capacity and price of the each vehicle,
 * and let them book a vehicle
 *
 * @author Terri Tai
 * @version 1.0
 */

public class VehiclesInfoFragment extends Fragment
{
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    public static Vehicle vehicle;
    public static ArrayList<Vehicle> vehiclesList;
    RecyclerView recVehicles;
    VehicleAdapter vehicleAdapter;
    View root;
    SwipeRefreshLayout swipeRefreshLayout;
    double price;
    double balance;

//    Setup UI layout, firebase instances and initialize vehicles ArrayList.

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_vehicles_info, container, false);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        vehiclesList = new ArrayList<>();
        getAndPopulateData();

        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                vehiclesList.clear();
                getAndPopulateData();
                vehicleAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

//    Get all of the vehicles from the database that are open.
//    Use document.toObject(Vehicle.class). This will deserialize the contents of the database information and give you a Vehicle object.
//    Add all vehicles to the vehicles ArrayList.
//    On completion of task for fetching all vehicles, set new RecyclerViewAdapter with the list of vehicles fetched.

    /**
     * This method gets vehicles from firestore collection called Vehicles and adds them to arraylist
     * called vehiclesList, and navigates to the chosen vehicle's vehiclesProfileActivity
     * if recycler view row is clicked
     */

    void getAndPopulateData()
    {
        try
        {
            firestore.collection("Vehicles").whereEqualTo("vehicleType", "Bicycle").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot document : task.getResult())
                        {
                            Bicycle bicycle = document.toObject(Bicycle.class);
                            if (bicycle.isOpen())
                            {
                                vehiclesList.add(bicycle);
                            }
                        }

                    }
                }
            });
            firestore.collection("Vehicles").whereEqualTo("vehicleType", "Segway").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot document : task.getResult())
                        {
                            Segway segway = document.toObject(Segway.class);
                            if (segway.isOpen())
                            {
                                vehiclesList.add(segway);
                            }
                        }

                    }
                }
            });
            firestore.collection("Vehicles").whereEqualTo("vehicleType", "Car").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot document : task.getResult())
                        {
                            Car car = document.toObject(Car.class);
                            if (car.isOpen())
                            {
                                vehiclesList.add(car);
                            }
                        }

                    }
                }
            });
            firestore.collection("Vehicles").whereEqualTo("vehicleType", "Helicopter").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot document : task.getResult())
                        {
                            Helicopter helicopter = document.toObject(Helicopter.class);
                            if (helicopter.isOpen())
                            {
                                vehiclesList.add(helicopter);
                            }
                        }
                    }
                    //set up adapter for vehicle recycler view
                    recVehicles = (RecyclerView) root.findViewById(R.id.recVehicles);
                    recVehicles.setLayoutManager(new LinearLayoutManager(getActivity()));
                    vehicleAdapter = new VehicleAdapter(vehiclesList, getActivity());
                    recVehicles.setAdapter(vehicleAdapter);
                    recVehicles.setItemAnimator(new DefaultItemAnimator());
                    try
                    {
                        vehicleAdapter.setOnItemClickListener(new VehicleAdapter.OnItemClickListener()
                        {
                            //set chosen vehicle to previously created vehicle object
                            //and send user to VehicleProfileActivity
                            @Override
                            public void onItemClick(int position)
                            {
                                vehicle = vehiclesList.get(position);
                                Intent intent = new Intent(getActivity(), VehicleProfileActivity.class);
                                startActivity(intent);
                            }
                            //call addUserToVehicle if icon is clicked
                            @Override
                            public void onAddClick(int position)
                            {
                                addUserToVehicle(position);
                            }
                        });
                    }
                    catch (Exception error)
                    {
                        //send fail message
                        Toast failure = Toast.makeText(root.getContext(), error.toString(), Toast.LENGTH_LONG);
                        failure.show();
                    }
                }
            });
        }
        catch (Exception error)
        {
            //send fail message
            Toast failure = Toast.makeText(root.getContext(), error.toString(), Toast.LENGTH_LONG);
            failure.show();
        }
    }

    /**
     * This method adds user to vehicle's riderUids and reduces the capacity by 1
     * if vehicle is available, and update chosen vehicle's fields in firestore
     * with the updated information
     *
     * @param position of the vehicle
     */
    void addUserToVehicle(int position)
    {
        if(vehiclesList.get(position).getCapacity() != 0)
        {
            String ID = vehiclesList.get(position).getVehicleID();
            int updatedCapacity = vehiclesList.get(position).getCapacity() - 1;
            ArrayList<String> updatedRiders = vehiclesList.get(position).getRiderUIDs();
            if (updatedRiders.contains(mUser.getUid()))
            {
                Toast fail = Toast.makeText(root.getContext(), "You have already booked a vehicle. Please await your vehicle to arrive.", Toast.LENGTH_LONG);
                fail.show();
            }
            else
            {
                updatedRiders.add(mUser.getUid());
                DocumentReference docRef = firestore.collection("Vehicles").document(ID);
                docRef.update("capacity", updatedCapacity);
                docRef.update("riderUIDs", updatedRiders);
            }
        }
        else
        {
            Toast fail = Toast.makeText(root.getContext(), "This vehicle is full. Waiting for owner to close.", Toast.LENGTH_LONG);
            fail.show();
        }
        firestore.collection("Users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot ds = task.getResult();
                    User user = ds.toObject(User.class);
                    balance = user.getBalance();
                    Log.d("PRICE", "times" + user.getPriceMultiplier());
                    price = vehiclesList.get(position).getBasePrice() * user.getPriceMultiplier();
                }
                else
                {
                    Log.d("USER", "Error: ", task.getException());
                }
            }
        });
        double updatedBalance = balance - price;
        DocumentReference docRef = firestore.collection("Users").document(mUser.getUid());
        docRef.update("balance", updatedBalance);
        Toast success = Toast.makeText(root.getContext(), "Successfully booked vehicle.", Toast.LENGTH_LONG);
        success.show();
    }

}