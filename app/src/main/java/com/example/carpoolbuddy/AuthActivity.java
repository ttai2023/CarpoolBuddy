package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This class provides methods that allow the user to
 * log in to their carpool buddy account using their
 * email and password
 *
 * @author Terri Tai
 * @version 1.0
 */

public class AuthActivity extends AppCompatActivity
{

    private int RC_SIGN_IN = 120;
    public static FirebaseAuth mAuth;
    public static FirebaseFirestore firestore;
    public static String name;
    public static String userType;
    public static String email;
    public static TextInputLayout emailLayout;
    public static TextView emailText;
    public static TextInputLayout passwordLayout;
    public static TextView passwordText;
    Button loginButton;
    Button signUpButton;


//    Set up Google sign in
//    Set up firebase auth and firestore instances
//    Setup up UI layout

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        emailLayout = (TextInputLayout) this.findViewById(R.id.emailLayout);
        emailText = this.findViewById(R.id.emailText);
        passwordLayout = (TextInputLayout) this.findViewById(R.id.passwordLayout);
        passwordText = this.findViewById(R.id.passwordText);
        loginButton = (Button) this.findViewById(R.id.login);
        signUpButton = (Button) this.findViewById(R.id.signUp);
    }

//    Check for the current user not null
//    If there is a user, go to the userProfileActivity or Main Activity

    @Override
    public void onStart()
    {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            updateUI(currentUser);
        }
    }

    /**
     * A method to check if the email editText is filled out
     * @return true or false depending on whether the editText was filled out
     */

    public boolean checkEmail()
    {
        email = emailText.getText().toString();
        if (email.isEmpty())
        {
            emailLayout.setError("Field can't be empty.");
            return false;
        }
        else
        {
            emailLayout.setError(null);
            return true;
        }
    }

    /**
     * A method to check if the password editText is filled out
     *
     * @return true or false depending on whether the editText was filled out
     */

    public boolean checkPassword()
    {
        String password = passwordText.getText().toString();
        if (password.isEmpty())
        {
            passwordLayout.setError("Field can't be empty.");
            return false;
        }
        else
        {
            passwordLayout.setError(null);
            return true;
        }
    }

//    This method uses the provided information to sign in the user.
//    On Task success, call updateUI()

    /**
     * A method that signs existing users in to their account
     * using firebase authentication using email and password
     * by pressing sign in button
     *
     * @param view of the user interface
     */

    public void signIn(View v)
    {
        if (!checkEmail() && !checkPassword())
        {
            return;
        }
        try
        {
            String password = passwordText.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            }
                            else
                            {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(AuthActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
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

//    This method uses the provided information to signUp the user.
//    On Task success, call updateUI()

    /**
     * leads the user to SignUpActivity so they can sign up
     * for an account after pressing the sign up button
     *
     * @param view of the user interface
     */

    public void signUp(View v)
    {
        Intent intent = new Intent(this, SignUpActivity.class);
        finish();
        startActivity(intent);
    }

//    This method will check if the user was created or was found.
//    Will allow the user to move to the rest of the app if sign in or sign up was successful.

    /**
     * leads the user to UserActivity after successfully logging in
     *
     * @param firebaseuser obtained from firebase authentication after user logs in
     */

    public void updateUI(FirebaseUser user)
    {
        if(user != null)
        {
            Intent intent = new Intent(this, UserActivity.class);
            finish();
            startActivity(intent);
        }
    }

}