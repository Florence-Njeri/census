package com.example.android.census2019.Activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.census2019.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class HouseHoldDataActivity extends AppCompatActivity {
    private static final String COUNTY = "county";
    private static final String SUB_COUNTY = "subCounty";
    private static final String TOWN = "town";
    private static final String ADULT_CHILDREN = "adultChildren";
    private static final String UNDERAGE_CHILDREN = "underageChildren";
    private static final String HEAD = "head";
    private static final String ID = "id";
    private static final String SPOUSES = "spouses";
    FirebaseFirestore db;
    TextInputEditText householdHead, headID, county, subCounty, town, adultChildren, underageChildren;
    Spinner spouses;
    Button saveButton;
    ImageButton audioRecording;
    TextView displayAudioText;
    private final String TAG = this.getClass().getSimpleName();
    private String mHeadOfTheHouse;
    private String mID;
    private String mNoOfSpouses;
    private String mNameOfCounty;
    private String mNameOfSubCounty;
    private String mNameOfTown;
    private String mNoOfAdultChildren;
    private String mNoOfUnderageChildren;
    private int AUDIO_CODE = 121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_hold_data);

        //    TODO: Upload the data to Firebase Firestore and show feedback message if successful or not,
        //    Also make the data persistent incase the user unexpectedly leaves the page
        saveButton = findViewById(R.id.saveButton);

        householdHead = findViewById(R.id.household_head);
        headID = findViewById(R.id.id);
        spouses = findViewById(R.id.spinnerSpouses);
        county = findViewById(R.id.county);
        subCounty = findViewById(R.id.sub_county);
        town = findViewById(R.id.town);
        adultChildren = findViewById(R.id.adult_children);
        underageChildren = findViewById(R.id.underage_children);

        audioRecording = findViewById(R.id.recordAudio);
        displayAudioText = findViewById(R.id.display_audio_text);

        //Set up the spinner for the no. of  spouses
        spousesSpinner();

        audioRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceRecording();
            }
        });
        //  Save button on click event code
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   First Check network connectivity if connected write to db else display toast

                ConnectivityManager checkConnection = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                assert checkConnection != null;
                NetworkInfo activeNetwork = checkConnection.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnected()) {
                    validate();
                } else {

                    Toast.makeText(HouseHoldDataActivity.this, "Ensure you are connected to the internet!!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //    Handle the  voice printing
    private void voiceRecording() {
        Intent audioIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        audioIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);


        audioIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        audioIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now ");

        try {
            startActivityForResult(audioIntent, AUDIO_CODE);
        } catch (ActivityNotFoundException notFound) {
            //
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

        if (requestCode == RESULT_OK && null != intent) {

            ArrayList <String> voiceText = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            displayAudioText.setText(voiceText.get(0));


        }
    }


    private void saveToDatabase() {
        //Initialize Firestore
        db = FirebaseFirestore.getInstance();

        //Add the agent input of the household data
        Map <String, Object> household = new HashMap <>();
        household.put(HEAD, mHeadOfTheHouse);
        household.put(ID, mID);
        household.put(SPOUSES, mNoOfSpouses);
        household.put(COUNTY, mNameOfCounty);
        household.put(SUB_COUNTY, mNameOfSubCounty);
        household.put(TOWN, mNameOfTown);
        household.put(ADULT_CHILDREN, mNoOfAdultChildren);
        household.put(UNDERAGE_CHILDREN, mNoOfUnderageChildren);
        //   Store input data in a document using collections
        db.collection("households")
                .add(household)
                //When you successfully add to firestore
                .addOnSuccessListener(new OnSuccessListener <DocumentReference>() {


                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Your code
                        Toast.makeText(HouseHoldDataActivity.this, "Data successfully updated to database", Toast.LENGTH_SHORT).show();
                    }
                })
                //   In case it fails to update
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Your code
                        Toast.makeText(HouseHoldDataActivity.this, "An error occurred when updating the database please retry", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void validate() {
        //Get the agents input
        mHeadOfTheHouse = Objects.requireNonNull(householdHead.getText()).toString();
        mID = Objects.requireNonNull(headID.getText()).toString();
        mNoOfSpouses = spouses.getSelectedItem().toString();
        mNameOfCounty = Objects.requireNonNull(county.getText()).toString();
        mNameOfSubCounty = Objects.requireNonNull(subCounty.getText()).toString();
        mNameOfTown = Objects.requireNonNull(town.getText()).toString();
        mNoOfAdultChildren = Objects.requireNonNull(adultChildren.getText()).toString();
        mNoOfUnderageChildren = Objects.requireNonNull(underageChildren.getText()).toString();

        boolean validationFail = false;

        if (mHeadOfTheHouse.length() == 0) {
            validationFail = true;
            householdHead.setError(getString(R.string.empty_error));
            householdHead.requestFocus();
        }
        if (mID.length() == 0) {
            validationFail = true;
            headID.setError(getString(R.string.empty_error));
            headID.requestFocus();
        }
        if (mNoOfSpouses.length() == 0) {
            validationFail = true;
            Toast.makeText(getApplicationContext(), R.string.spinner_error, Toast.LENGTH_LONG).show();
            spouses.requestFocus();
        }
        if (mNameOfCounty.length() == 0) {
            validationFail = true;
            county.setError(getString(R.string.empty_error));
            county.requestFocus();
        }
        if (mNameOfSubCounty.length() == 0) {
            validationFail = true;
            subCounty.setError(getString(R.string.empty_error));
            subCounty.requestFocus();
        }
        if (mNameOfTown.length() == 0) {
            validationFail = true;
            subCounty.setError(getString(R.string.empty_error));
            subCounty.requestFocus();
        }
        if (mNoOfAdultChildren.length() == 0) {
            validationFail = true;
            adultChildren.setError(getString(R.string.empty_error));
            adultChildren.requestFocus();
        }
        if (mNoOfUnderageChildren.length() == 0) {
            validationFail = true;
            underageChildren.setError(getString(R.string.empty_error));
            underageChildren.requestFocus();
        }
//If all are valid then,
        if (validationFail == false) {
            saveToDatabase();
        }

    }


    private void spousesSpinner() {
        //Populate the spinner with data
        Spinner spinner = findViewById(R.id.spinnerSpouses);
        ArrayAdapter <CharSequence> spouses = ArrayAdapter.createFromResource(this, R.array.spouses_arrays, android.R.layout.simple_spinner_item);
        //Display the array as a dropDown list
        spouses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spouses);
    }
}
