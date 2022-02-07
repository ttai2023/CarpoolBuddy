package com.example.carpoolbuddy;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

/**
 * This class provides methods that allow the user to
 * navigate between fragments through a drawer
 *
 * @author Terri Tai
 * @version 1.0
 */

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    DrawerLayout userDrawer;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //create a drawer displaying all pages
        userDrawer = findViewById(R.id.user_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //create a header for the drawer displaying the user's name and type
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = (TextView) headerView.findViewById(R.id.header_userName);
        TextView navUserType = (TextView) headerView.findViewById(R.id.header_userType);
        firestore.collection("Users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    DocumentSnapshot ds = task.getResult();
                    User user = ds.toObject(User.class);
                    String userName = user.getName();
                    String userType = user.getUserType();
                    navUserName.setText(userName);
                    navUserType.setText(userType);
                }
            }
        });

        //set a navigation on item click listener so the app can switch to the selected fragment
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, userDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close); //open and closes navigation drawer
        userDrawer.addDrawerListener(toggle);
        toggle.syncState(); //rotate icon with drawer

        if (savedInstanceState == null) //if device does not get rotated (as it will start oncreate again)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new VehiclesInfoFragment()).commit(); //open to first fragment (vehicles rec view)
            navigationView.setCheckedItem(R.id.nav_vehicles); //set initial item to vehicles rec view
        }
    }

    /**
     * switches fragments according to which fragment
     * the user chooses to go to on navigation drawer
     *
     * @param menuItem that is selected from the navigation drawer
     * @return true if switching is successful
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        //check which fragment gets selected by user
        switch (menuItem.getItemId())
        {
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UserProfileFragment()).commit();
                break;
            case R.id.nav_vehicles:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new VehiclesInfoFragment()).commit();
                break;

            case R.id.nav_addVehicle:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddVehicleFragment()).commit();
                break;

            case R.id.nav_userVehicles:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UserVehiclesFragment()).commit();
                break;
        }
        //if drawer is closed, close it to the left of the page
        userDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * if back button is pressed, close the drawer if it is open
     */
    public void onBackPressed() //if back button is pressed
    {
        if (userDrawer.isDrawerOpen(GravityCompat.START)) //if drawer open
        {
            userDrawer.closeDrawer(GravityCompat.START); //close drawer
        }
        else
        {
            super.onBackPressed();
        }
    }

}