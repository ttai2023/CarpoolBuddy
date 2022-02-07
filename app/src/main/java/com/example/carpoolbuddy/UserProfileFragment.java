package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.rpc.context.AttributeContext;

import org.w3c.dom.Text;

/**
 * This class provides methods that allow the user to
 * view their name and the type of user they are
 * and allow them to sign out of their account
 *
 * @author Terri Tai
 * @version 1.0
 */

public class UserProfileFragment extends androidx.fragment.app.Fragment
{

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore firestore;
    TextView userName;
    TextView userType;
    TextView balanceText;
    String name;
    String type;
    double balance;
    Button signOut;
    View root;

//    Setup UI layout, firebase instances and display info for textViews

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_user_profile, container, false);
        signOut = (Button) root.findViewById(R.id.signOut);

        //signs the user out of their account if sign out button is clicked
        signOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), AuthActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        userName = getActivity().findViewById(R.id.userName);
        userType = getActivity().findViewById(R.id.userType);
        balanceText = getActivity().findViewById(R.id.balance);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        getUserInfo();

    }

//    Called by a button. Calls mAuth.signOut() to sign the user out of their account.
//    Create intent to go back to AuthActivity after signout complete.

    /**
     * This method finds the user in firestore collection called Users using the
     * current user's id and make a user object accordingly while
     * setting userName and userType textviews to the user's name and type
     */

    public void getUserInfo()
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
                        name = user.getName();
                        type = user.getUserType();
                        balance = user.getBalance();
                        balanceText.setText("Balance: $" + String.valueOf(balance));
                        userName.setText(name);
                        userType.setText(type);
                    }
                    else
                    {
                        Toast fail = Toast.makeText(root.getContext(), task.getException().toString(), Toast.LENGTH_LONG);
                        fail.show();
                    }
                }
            });
        }
        catch(Exception error)
        {
            Toast fail = Toast.makeText(root.getContext(), error.toString(), Toast.LENGTH_LONG);
            fail.show();
        }

    }

}