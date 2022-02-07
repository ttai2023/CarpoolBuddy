package com.example.carpoolbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This class provides methods that allow the user to
 * view their vehicles, including each vehicle's model, capacity and status
 *
 * @author Terri Tai
 * @version 1.0
 */

public class UserVehiclesFragment extends Fragment
{
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    public static Vehicle userVehicle;
    public static ArrayList<Vehicle> userVehiclesList;
    RecyclerView recVehicles;
    UserVehiclesAdapter userVehiclesAdapter;
    View root;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_user_vehicles, container, false);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        setUpVehicles();

        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * This method collects all the vehicles from the firebase collection called Vehicles
     * where the owner field matches current user id, and sets up a recycler view only
     * displaying user's vehicles
     *
     */
    void setUpVehicles()
    {
        userVehiclesList = new ArrayList<>();
        firestore.collection("Vehicles").whereEqualTo("owner", mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                for (QueryDocumentSnapshot document : task.getResult())
                {
                    Vehicle vehicle = document.toObject(Vehicle.class);
                    userVehiclesList.add(vehicle);
                }
                recVehicles = root.findViewById(R.id.recUsers);
                recVehicles.setLayoutManager(new LinearLayoutManager(getActivity()));
                userVehiclesAdapter = new UserVehiclesAdapter(userVehiclesList, getActivity());
                recVehicles.setItemAnimator(new DefaultItemAnimator());
                recVehicles.setAdapter(userVehiclesAdapter);

                try
                {
                    //set up adapter for menu recycler view
                    userVehiclesAdapter.setOnItemClickListener(new UserVehiclesAdapter.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(int position)
                        {
                            userVehicle = userVehiclesList.get(position);
                            Intent intent = new Intent(getActivity(), VehicleProfileActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                catch (Exception error)
                {
                    //send fail message
                    Toast failure = Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);
                    failure.show();
                }
            }
        });
    }


}
