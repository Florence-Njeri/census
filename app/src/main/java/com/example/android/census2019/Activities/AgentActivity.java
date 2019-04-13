package com.example.android.census2019.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.census2019.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AgentActivity extends AppCompatActivity {
    private static final String ID = "id_agent";
    private static final String COUNTY = "county";
    private static final String SUB_COUNTY = "subCounty";
    private static final String PHONE = "phone";
    private static final String EDUCATION = "education";
    private static final String JOB_TITLE = "job_title";
    FirebaseFirestore mFirebaseFirestore;
    Button save;
    Spinner position, education;
    TextInputEditText phone, id, county, sub_county;
    private String mJobTitle;
    private String mEducationLevel;
    private String mID;
    private String mNameOfCounty;
    private String mNameOfSubCounty;
    private String mPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);
//        Find the views by ID

        position = findViewById(R.id.spinnerJobTitle);
        education = findViewById(R.id.spinnerEducation);
        phone = findViewById(R.id.agent_phone);
        id = findViewById(R.id.agent_id);
        county = findViewById(R.id.agent_county);
        sub_county = findViewById(R.id.sub_county);

        save = findViewById(R.id.saveAgentData);
        //        Initialize firestore
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Perform validation and check if data is successfully saved then open main activity
                saveToDatabase();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));


            }
        });
        spousesSpinner();
    }

    private void saveToDatabase() {


        //Get the input data
        mJobTitle = position.getSelectedItem().toString().trim();
        mEducationLevel = education.getSelectedItem().toString().trim();
        mID = Objects.requireNonNull(id.getText()).toString().trim();
        mNameOfCounty = Objects.requireNonNull(county.getText()).toString().trim();
        mNameOfSubCounty = Objects.requireNonNull(sub_county.getText()).toString().trim();
        mPhoneNumber = Objects.requireNonNull(phone.getText()).toString().trim();

        Map <String, String> agentData = new HashMap <>();
        agentData.put(JOB_TITLE, mID);
        agentData.put(EDUCATION, mID);
        agentData.put(ID, mID);
        agentData.put(COUNTY, mNameOfCounty);
        agentData.put(SUB_COUNTY, mNameOfSubCounty);
        agentData.put(PHONE, mPhoneNumber);

//        Store data using a collection

        mFirebaseFirestore.collection("agent")
                .add(agentData)
                .addOnSuccessListener(new OnSuccessListener <DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AgentActivity.this, "Data successfully inserted to the database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AgentActivity.this, "Data failed to insert to the database", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void validate() {
        if (mJobTitle.isEmpty()) {
            Toast.makeText(getApplicationContext(),  R.string.spinner_error, Toast.LENGTH_LONG).show();
            position.requestFocus();
        }
        else if (mID.isEmpty()) {
            id.setError(getString(R.string.empty_error));
            id.requestFocus();
        }
        else if (mEducationLevel.isEmpty()) {

            Toast.makeText(getApplicationContext(),  R.string.spinner_error, Toast.LENGTH_LONG).show();
            education.requestFocus();
        }
        else if (mNameOfCounty.isEmpty()) {
            county.setError(getString(R.string.empty_error));
            county.requestFocus();
        }
        else if (mNameOfSubCounty.isEmpty()) {
            sub_county.setError(getString(R.string.empty_error));
            sub_county.requestFocus();
        } else if (mPhoneNumber.isEmpty()) {
            phone.setError(getString(R.string.empty_error));
            phone.requestFocus();
        }


    }

    private void spousesSpinner() {
        //Populate the spinner with data
        Spinner spinnerJobPosition = findViewById(R.id.spinnerJobTitle);
        Spinner spinnerEducationLevel = findViewById(R.id.spinnerEducation);
//        TODO: Remove repetition
        ArrayAdapter <CharSequence> jobPosition = ArrayAdapter.createFromResource(this, R.array.spouses_arrays, android.R.layout.simple_spinner_item);
        //Display the array as a dropDown list
        jobPosition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJobPosition.setAdapter(jobPosition);
        ArrayAdapter <CharSequence> educationLevel = ArrayAdapter.createFromResource(this, R.array.spouses_arrays, android.R.layout.simple_spinner_item);
        //Display the array as a dropDown list
        educationLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEducationLevel.setAdapter(educationLevel);
    }
}
