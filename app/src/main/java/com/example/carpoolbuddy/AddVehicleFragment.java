package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * This class provides methods that allow the user to
 * add a new vehicle to the carpool buddy database
 *
 * @author Terri Tai
 * @version 1.0
 */

public class AddVehicleFragment extends Fragment
{

    FirebaseFirestore firestoreRef;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    String vehicleID;
    View root;
    TextInputLayout modelLayout;
    TextInputEditText modelText;
    TextInputLayout capacityLayout;
    TextInputEditText capacityText;
    String [] vehicleTypes;
    String vehicleType;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    TextInputLayout priceLayout;
    TextInputEditText priceText;
    TextInputLayout optionLayout;
    TextInputEditText optionText;
    Button addVehicle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_add_vehicle, container, false);
        // Inflate the layout for this fragment
        addVehicle = (Button) root.findViewById(R.id.addVehicleButton);

        //calls addVehicle if formValid() returns true when user clicks addVehicle button
        addVehicle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (formValid())
                {
                    addVehicle();
                }
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        firestoreRef = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        modelText = root.findViewById(R.id.modelText);
        capacityText = root.findViewById(R.id.capacityText);
        priceText = root.findViewById(R.id.priceText);
        optionText = root.findViewById(R.id.optionText);
        modelLayout = root.findViewById(R.id.modelLayout);
        capacityLayout = root.findViewById(R.id.capacityLayout);
        priceLayout = root.findViewById(R.id.priceLayout);
        optionLayout = root.findViewById(R.id.optionLayout);

        vehicleTypes = new String[]{"Car", "Bicycle", "Helicopter", "Segway"};
        autoCompleteTextView = root.findViewById(R.id.autoCompleteTextView);
        adapterItems = new ArrayAdapter<String>(root.getContext(), R.layout.dropdown_item, vehicleTypes);
        autoCompleteTextView.setAdapter(adapterItems);
        //sets optionText's hint according to the type of vehicle that is chosen
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                vehicleType = parent.getItemAtPosition(position).toString();
                if(vehicleType.equals("Car"))
                {
                    optionText.setHint("Range");
                }
                if(vehicleType.equals("Bicycle"))
                {
                    optionText.setHint("Bicycle Type, Weight, Weight Capacity");
                }
                if(vehicleType.equals("Helicopter"))
                {
                    optionText.setHint("Max Altitude, Max Air Speed");
                }
                if(vehicleType.equals("Segway"))
                {
                    optionText.setHint("Range, Weight Capacity");
                }
            }
        });

    }

//    Checks all of the textEdits and returns true if they all include valid info.
//    Show toast on error.

    /**
     * checks if all the fields required is filled out by the user
     * @return true or false depending if fields are filled out
     */
    boolean formValid()
    {
        if(checkModel() && checkCapacity() && checkPrice() && checkOption())
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public boolean checkModel()
    {
        String model = modelText.getText().toString();
        if(model.isEmpty())
        {
            modelLayout.setError("Field can't be empty.");
            return false;
        }
        else
        {
            modelLayout.setError(null);
            return true;
        }
    }

    public boolean checkCapacity()
    {
        String capacity = capacityText.getText().toString();
        if (capacity.isEmpty())
        {
            capacityLayout.setError("Field can't be empty.");
            return false;
        }
        else
        {
            capacityLayout.setError(null);
            return true;
        }
    }

    public boolean checkPrice()
    {
        String price = priceText.getText().toString();
        if (price.isEmpty())
        {
            priceLayout.setError("Field can't be empty.");
            return false;
        }
        else
        {
            priceLayout.setError(null);
            return true;
        }

    }

    public boolean checkOption()
    {
        String option = priceText.getText().toString();
        if (option.isEmpty())
        {
            optionLayout.setError("Field can't be empty.");
            return false;
        }
        else
        {
            optionLayout.setError(null);
            return true;
        }

    }

//    Call formValid to check if input is ok.
//    Check the type of vehicle chosen, use the database reference to store the vehicle subclass instance.
//    If a user adds a Car, upload a Car object, if user adds an Electric Car upload an ElectricCar object.
//    Go to VehiclesInfoActivity on success. Show toast on failure.

    /**
     * creates a new vehicle and adds it to Vehicles collection
     * in firebase and set the document to the vehicles ID,
     * calls addVehicleToUser when vehicle is added to firebase
     */

    void addVehicle()
    {
        String model = modelText.getText().toString();
        int capacity = Integer.valueOf(capacityText.getText().toString());
        ArrayList<String> riders = new ArrayList<>();
        riders.add(mUser.getUid());
        int basePrice = Integer.valueOf(priceText.getText().toString());

        firestoreRef.collection("Vehicles").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (vehicleType.equals("Car"))
                {
                    int range = Integer.parseInt(optionText.getText().toString());
                    Car car = new Car(mUser.getUid(), model, capacity, riders, true, vehicleType, basePrice, range);
                    vehicleID = car.getVehicleID();
                    AuthActivity.firestore.collection("Vehicles").document(car.getVehicleID()).set(car);
                    addVehicleToUser();
                    Toast success = Toast.makeText(root.getContext(), "Successfully added car to user account.", Toast.LENGTH_LONG);
                    success.show();
                }
                else if (vehicleType.equals("Bicycle"))
                {
                    String allInfo = optionText.getText().toString();
                    String[] data = allInfo.split(",", 3);
                    String bicycleType = data[0];
                    int weight = Integer.parseInt(data[1]);
                    int weightCapacity = Integer.parseInt(data[2]);
                    Bicycle bicycle = new Bicycle(mUser.getUid(), model, capacity, riders, true, vehicleType, basePrice, bicycleType, weight, weightCapacity);
                    vehicleID = bicycle.getVehicleID();
                    AuthActivity.firestore.collection("Vehicles").document(bicycle.getVehicleID()).set(bicycle);
                    Toast success = Toast.makeText(root.getContext(), "Successfully added bicycle to user account.", Toast.LENGTH_LONG);
                    success.show();
                    addVehicleToUser();
                }
                else if (vehicleType.equals("Helicopter"))
                {
                    String allInfo = optionText.getText().toString();
                    String[] data = allInfo.split(",", 2);
                    int maxAltitude = Integer.parseInt(data[0]);
                    int maxAirSpeed = Integer.parseInt(data[1]);
                    Helicopter helicopter = new Helicopter(mUser.getUid(), model, capacity, riders, true, vehicleType, basePrice, maxAltitude, maxAirSpeed);
                    vehicleID = helicopter.getVehicleID();
                    AuthActivity.firestore.collection("Vehicles").document(helicopter.getVehicleID()).set(helicopter);
                    Toast success = Toast.makeText(root.getContext(), "Successfully added helicopter to user account.", Toast.LENGTH_LONG);
                    success.show();
                    addVehicleToUser();
                }
                else
                {
                    String allInfo = optionText.getText().toString();
                    String[] data = allInfo.split(",", 2);
                    int range = Integer.parseInt(data[0]);
                    int weightCapacity = Integer.parseInt(data[1]);
                    Segway segway = new Segway(mUser.getUid(), model, capacity, riders, true, vehicleType, basePrice, range, weightCapacity);
                    vehicleID = segway.getVehicleID();
                    AuthActivity.firestore.collection("Vehicles").document(segway.getVehicleID()).set(segway);
                    Toast success = Toast.makeText(root.getContext(), "Successfully added segway to user account.", Toast.LENGTH_LONG);
                    success.show();
                    addVehicleToUser();
                }
            }
        });
    }

    /**
     * add newly added vehicle to users ownedVehicles field in firebase
     */
    void addVehicleToUser()
    {
        firestoreRef.collection("Users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    DocumentSnapshot ds = task.getResult();
                    User user = ds.toObject(User.class);
                    if (user.getUserType().equals("Student"))
                    {
                        Student student = ds.toObject(Student.class);
                        student.getOwnedVehicles().add(vehicleID);
                        firestoreRef.collection("Users").document(mUser.getUid()).set(student);
                    }
                    else if (user.getUserType().equals("Teacher"))
                    {
                        Teacher teacher = ds.toObject(Teacher.class);
                        teacher.getOwnedVehicles().add(vehicleID);
                        firestoreRef.collection("Users").document(mUser.getUid()).set(teacher);
                    }
                    else if (user.getUserType().equals("Parent"))
                    {
                        Parent parent = ds.toObject(Parent.class);
                        parent.getOwnedVehicles().add(vehicleID);
                        firestoreRef.collection("Users").document(mUser.getUid()).set(parent);
                    }
                    else
                    {
                        Alumni alumni = ds.toObject(Alumni.class);
                        alumni.getOwnedVehicles().add(vehicleID);
                        firestoreRef.collection("Users").document(mUser.getUid()).set(alumni);
                    }
                }
                else
                {
                    Toast failure = Toast.makeText(root.getContext(), "Failed to find user. Error: " + task.getException(), Toast.LENGTH_LONG);
                    failure.show();
                }
            }

        });
    }
}
