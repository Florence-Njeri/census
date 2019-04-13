package com.example.android.census2019.Activities;

import android.content.Intent;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.census2019.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class LoginActivity extends AppCompatActivity  {
    private static final int RC_SIGN_IN = 1;

    private FirebaseDatabase mDatabase;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        /*
         * TODO: Implement the LogIn Screen Using Firebase Authentication
         */
//        Initialize FireBase Authentication
        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        // Choose authentication providers
        setUpFirebase();


    }

    private void setUpFirebase() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


        // Create and launch sign-in intent

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }


    //    Receive the result of the sign in
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                // If successfully signed in, go to the MainActivity
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user==null){
                    setUpFirebase();

                }
                //     For a new user
                else{
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                }
            }
            else {
              Toast.makeText(this,getString(R.string.sign_in_error),Toast.LENGTH_SHORT).show();
            }
        }
    }

 }

