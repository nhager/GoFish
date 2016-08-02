package com.example.nh612u.gofish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.*;
import android.widget.Toast;
import android.content.Intent;

public class AdminViewActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_admin_view);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (groupPosition == 0) {
                    if (childPosition == 0) {
                        startActivity(new Intent(AdminViewActivity.this, RegisterUserActivity.class));
                    }
                    if (childPosition == 1) {
                        startActivity(new Intent(AdminViewActivity.this, DeleteActivity.class));
                    }
                }
                if (groupPosition == 1) {
                    startActivity(new Intent(AdminViewActivity.this, InventoryActivity.class));
                }
                if (groupPosition == 2) {
                    if (childPosition == 0) {
                        startActivity(new Intent(AdminViewActivity.this, CreateEventActivity.class));
                    }
                    if (childPosition == 1) {
                        startActivity(new Intent(AdminViewActivity.this, DeleteEventActivity.class));
                    }
                    if (childPosition == 2) {
                        startActivity(new Intent(AdminViewActivity.this, DeleteEventActivity.class));
                    }
                    if (childPosition == 3) {
                        startActivity(new Intent(AdminViewActivity.this, MapMainActivity.class));
                    }
                }
                return false;
            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Users");
        listDataHeader.add("Equipment");
        listDataHeader.add("Events");

        // Adding child data
        List<String> userOpts = new ArrayList<String>();
        userOpts.add("Create user");
        userOpts.add("Delete user");

        List<String> equipOpts = new ArrayList<String>();
        equipOpts.add("Inventory");

        List<String> eventOpts = new ArrayList<String>();
        eventOpts.add("Create event");
        eventOpts.add("Delete event");
        eventOpts.add("View events");
        eventOpts.add("View map");

        listDataChild.put(listDataHeader.get(0), userOpts); // Header, Child data
        listDataChild.put(listDataHeader.get(1), equipOpts);
        listDataChild.put(listDataHeader.get(2), eventOpts);
    }
}