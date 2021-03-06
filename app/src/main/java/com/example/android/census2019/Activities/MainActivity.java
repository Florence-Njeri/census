package com.example.android.census2019.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.census2019.Agent;
import com.example.android.census2019.BuildConfig;
import com.example.android.census2019.Household;
import com.example.android.census2019.HouseholdAdapter;
import com.example.android.census2019.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout mDrawer;
    ArrayList <Household> list = new ArrayList <>();
    HouseholdAdapter mAdapter;
    FirebaseFirestore mFirebaseFirestore;
    private String TAG = "Main Activity";
    private RecyclerView mRecyclerView;
    public String mId_agent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set the toolBar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initialize firestore
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        firestoreDateObject();
        //        RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        //Initialize and set up the adapter
        mAdapter = new HouseholdAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //      TODO: Open a new activity so you can display household details
                startActivity(new Intent(getApplicationContext(), HouseHoldDataActivity.class));
            }
        });

        //    TODO:    Update the view with some of the household data in a RecyclerView from the FireStore data
        readFromDatabase();

        headerData(navigationView);
        enableStrictMode();

    }

    private void firestoreDateObject() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirebaseFirestore.setFirestoreSettings(settings);
    }

    private void enableStrictMode() {
        //Only run when debugging or testing
        if (BuildConfig.DEBUG) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    private void headerData(NavigationView navigationView) {

        View headerView = navigationView.getHeaderView(0);
        final TextView agent_job = headerView.findViewById(R.id.agent_job);
        final TextView agent_id = headerView.findViewById(R.id.header_id);
        final TextView agent_county = headerView.findViewById(R.id.header_county);


        //        Read agent data from firestore
        mFirebaseFirestore.collection("agent")
                .addSnapshotListener(new EventListener <QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        assert queryDocumentSnapshots != null;
                        if (queryDocumentSnapshots.isEmpty()) {
                            //     If the query returns a null document go to LogIn activity so they can sign up
                            startActivity(new Intent(getApplicationContext(), AgentActivity.class));
                        }
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            Agent agentData = doc.getDocument().toObject(Agent.class);
                            String job = agentData.getJob_title();
                            mId_agent = agentData.getId_agent();
                            String county = agentData.getCounty();
                            //        Household inventoryData = doc.getDocument().toObject(Household.class);
                            Log.d(TAG, "DATA:" + agentData);
                            agent_job.setText(job);
                            agent_id.setText(mId_agent);
                            agent_county.setText(county);

                        }

                    }
                });
    }


    private void readFromDatabase() {

        //        Read from firestore
        mFirebaseFirestore.collection("households")
                .addSnapshotListener(new EventListener <QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        assert queryDocumentSnapshots != null;
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            Household household = doc.getDocument().toObject(Household.class);

                            Log.d(TAG, "DATA:" + household);

                            list.add(household);

                            Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();

                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {
        //Action to perform when the user presses the back button

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            //   Sign out of the application
            AuthUI.getInstance().signOut(this);
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            Toast.makeText(this, "You are signed out!!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_profile) {
            Toast.makeText(this, "Agent Profile Census 2019", Toast.LENGTH_SHORT).show();

        }


        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
