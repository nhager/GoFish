package com.example.nh612u.gofish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class InventoryActivity extends AppCompatActivity {

    Spinner eventSelect;
    Spinner typeSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] events = getEvents();
        eventSelect = (Spinner) findViewById(R.id.typeFilterSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, events);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSelect.setAdapter(dataAdapter);

        final Button createItemButton = (Button) findViewById(R.id.createItemButton);
        createItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryActivity.this, CreateItemActivity.class));
            }
        });

        final Button inventoryButton = (Button) findViewById(R.id.assignItemButton);
        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryActivity.this, AssignItemActivity.class));
            }
        });
    }

    //TODO: Get user list from database.
    //TODO: User user object instead of string.
    public String[] getTypes(){
        String[] strs = {"Kayak","Buffalo","Candy"};
        return strs;
    }

    //TODO: Get user list from database.
    //TODO: User user object instead of string.
    public String[] getEvents(){
        String[] strs = {"David Purcell","Phillp-a","Bilanco"};
        return strs;
    }
}
