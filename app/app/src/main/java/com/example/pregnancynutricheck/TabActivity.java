package com.example.pregnancynutricheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TabActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private ViewPageAdapter adapter;
    private TabLayout tabLayout;

    String phone = "";
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        phone = user.get(sessionManager.PHONE);

//        Spinner spinner = (Spinner) findViewById(R.id.spinner);
//
//        // Spinner click listener
//        spinner.setOnItemSelectedListener(this);
//
//         Spinner Drop down elements
//        List<String> categories = new ArrayList<String>();
//        categories.add("English");
//        categories.add("Marathi");
//
//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spinner.setAdapter(dataAdapter);



        viewPager=findViewById(R.id.pager);
        adapter=new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout=findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(getApplicationContext(), rating.class);
                startActivity(intent);
                return true;
//                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
            case R.id.item2:
                sessionManager.logout();
//                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
