package com.example.nh612u.gofish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        eventSelect = (Spinner) findViewById(R.id.eventFilterSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, events);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSelect.setAdapter(dataAdapter);

        String[] types = getTypes();
        typeSelect = (Spinner) findViewById(R.id.typeFilterSpinner);
        ArrayAdapter<String> typeDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, types);
        typeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSelect.setAdapter(typeDataAdapter);

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

        //http://www.vogella.com/tutorials/AndroidListView/article.html#androidlists
        final ListView listview = (ListView) findViewById(R.id.inventoryList);
        String[] values = getInventory();

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0);
            }

        });
    }

    //TODO: Get type list from database.
    public String[] getTypes(){
        String[] strs = {"Kayak","Buffalo","Candy"};
        return strs;
    }

    //TODO: Get event list from database.
    //TODO: User event object instead of string.
    public String[] getEvents(){
        String[] strs = {"David Purcell","Phillp-a","Blanco"};
        return strs;
    }

    //TODO: Get inventory from database.
    //TODO: Item object instead of string.
    public String[] getInventory(){
        String[] strs = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
        return strs;
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}