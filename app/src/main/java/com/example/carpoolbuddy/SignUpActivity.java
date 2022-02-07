package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import lombok.val;

/**
 * This class provides methods that allow the user to
 * sign up for a new account using their email and password
 *
 * @author Terri Tai
 * @version 1.0
 */

public class SignUpActivity extends AppCompatActivity
{
    String selected;
    String [] userTypes;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    public static TextInputLayout nameLayout;
    public static TextView nameText;
    public static TextInputLayout optionLayout;
    public static TextView optionText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameText = this.findViewById(R.id.nameText);
        optionText = this.findViewById(R.id.optionText);

        userTypes = new String[]{"Student", "Teacher", "Parent", "Alumni"};
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        adapterItems = new ArrayAdapter<String>(this, R.layout.dropdown_item, userTypes);
        autoCompleteTextView.setAdapter(adapterItems);

        //set hint of optionText depending on what role the user picks
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                selected = parent.getItemAtPosition(position).toString();
                Log.d("TYPE", selected);
                if(selected.equals("Student"))
                {
                    optionText.setHint("Graduating Year");
                }
                if(selected.equals("Teacher"))
                {
                    optionText.setHint("In School Title");
                }
                if(selected.equals("Parent"))
                {
                    optionText.setHint("Children");
                }
                if(selected.equals("Alumni"))
                {
                    optionText.setHint("Graduate Year");
                }

            }
        });

    }

    /**
     * A method to check if the name editText is filled out
     * @return true or false depending on whether the editText was filled out
     */
    public boolean checkName()
    {
        String name = nameText.getText().toString();
        if (name.isEmpty())
        {
            nameLayout.setError("Field can't be empty.");
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * A method to check if the option editText (depending on the role of the user) is filled out
     * @return true or false depending on whether the editText was filled out
     */
    public boolean checkOption()
    {
        String option = optionText.getText().toString();
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

    /**
     * This method creates a user with email and password using firebase authentication,
     * creates a user object depending on the type of user and
     * adds the created user object to Users collection in firebase
     *
     * @param view of the user interface
     */

    public void signUp(View v)
    {
        if(!checkName() && !checkOption())
        {
            return;
        }
        try
        {
            String name = nameText.getText().toString();
            String email = AuthActivity.emailText.getText().toString();
            String password = AuthActivity.passwordText.getText().toString();
            AuthActivity.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast success = Toast.makeText(getBaseContext(), "User successfully signed up", Toast.LENGTH_LONG);
                        success.show();
                        FirebaseUser currentUser = AuthActivity.mAuth.getCurrentUser();
                        if (selected.equals("Student"))
                        {
                            String graduationYear = optionText.getText().toString();
                            Student student = new Student(currentUser.getUid(), name, email, selected, 0.5, new ArrayList<String>(), graduationYear, new ArrayList<String>());
                            AuthActivity.firestore.collection("Users").document(currentUser.getUid()).set(student);

                        }
                        else if (selected.equals("Teacher"))
                        {
                            String inSchoolTitle = optionText.getText().toString();
                            Teacher teacher = new Teacher(currentUser.getUid(), name, email, selected, 0.5, new ArrayList<String>(), inSchoolTitle);
                            AuthActivity.firestore.collection("Users").document(currentUser.getUid()).set(teacher);
                        }
                        else if (selected.equals("Parent"))
                        {
                            String children = optionText.getText().toString();
                            ArrayList<String> childrenList;
                            if(children.contains(","))
                            {
                                String[] childrenNames = children.split(",");
                                childrenList = new ArrayList<String>(Arrays.asList(childrenNames));
                            }
                            else
                            {
                                childrenList = new ArrayList<String>();
                                childrenList.add(children);
                            }
                            Parent parent = new Parent(currentUser.getUid(), name, email, selected, 1, new ArrayList<String>(), childrenList);
                            AuthActivity.firestore.collection("Users").document(currentUser.getUid()).set(parent);
                        }
                        else
                        {
                            String graduateYear = optionText.getText().toString();
                            Alumni alumni = new Alumni(currentUser.getUid(), name, email, selected, 1, new ArrayList<String>(), graduateYear);
                            AuthActivity.firestore.collection("Users").document(currentUser.getUid()).set(alumni);
                        }
                        finishSignUp();

                    }
                    else
                    {
                        Toast failure = Toast.makeText(getBaseContext(), "Creating user failed. Error: " + task.getException(), Toast.LENGTH_LONG);
                        failure.show();
                    }
                }
            });
        }
        catch (Exception error)
        {
            //send fail message
            Toast failure = Toast.makeText(this, "Error: " + error.toString(), Toast.LENGTH_LONG);
            failure.show();
        }
    }

    /**
     * This method sends user to UserActivity after they successfully signed up for an account
     */

    public void finishSignUp()
    {
        Intent intent = new Intent(this, UserActivity.class);
        finish();
        startActivity(intent);
    }

}