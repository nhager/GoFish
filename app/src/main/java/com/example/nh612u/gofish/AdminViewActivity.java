package com.example.nh612u.gofish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.*;
import android.widget.Toast;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminViewActivity extends AppCompatActivity {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private List<JSONObject> eventSignUps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_admin_view);
        getEventsForCurrentUser();
    }

    private void getEventsForCurrentUser() {
        HttpHelper httpHelper = new HttpHelper(getEventsCurrentUserHasJoined());
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("user_id", getIntent().getStringExtra("user_id"));
            System.out.println(getIntent().getStringExtra("user_id"));
            httpHelper.GET(HttpHelper.TABLE.EVENT_SIGNUP, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Handler.Callback getEventsCurrentUserHasJoined() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                if (response == null || response.contains("error")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Error retrieving event data from server.", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    System.out.println(response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            eventSignUps.add(jsonArray.getJSONObject(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    generateView();
                }
                return true;
            }
        };
        return callback;
    }

    private void generateView() {
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
                    try {
                        System.out.println(eventSignUps.get(childPosition).getString("event_id"));
                        getIntent().putExtra("event_id", eventSignUps.get(childPosition).getString("event_id"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startActivity(IntentHelper.createNewIntent(getIntent(), AdminViewActivity.this,
                            MapMainActivity.class));
                }
                if (groupPosition == 3) {
                    if (childPosition == 0) {
                        startActivity(new Intent(AdminViewActivity.this, CreateEventActivity.class));
                    }
                    if (childPosition == 1) {
                        startActivity(IntentHelper.createNewIntent(getIntent(), AdminViewActivity.this,
                                JoinEvent.class));
                    }
                    if (childPosition == 2) {
                        startActivity(new Intent(AdminViewActivity.this, DeleteEventActivity.class));
                    }
                    if (childPosition == 3) {
                        startActivity(new Intent(AdminViewActivity.this, ViewEvents.class));
                    }
                }
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousItem )
                    expListView.collapseGroup(previousItem );
                previousItem = groupPosition;
            }
        });
    }

    //Preparing list data

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Users");
        listDataHeader.add("Equipment");
        listDataHeader.add("Events");
        listDataHeader.add("Event Options");

        // Adding child data
        List<String> userOpts = new ArrayList<String>();
        userOpts.add("Create user");
        userOpts.add("Delete user");

        List<String> equipOpts = new ArrayList<String>();
        equipOpts.add("Inventory");

        List<String> eventOpts = new ArrayList<String>();
        try {
            for (JSONObject jsonEvent : eventSignUps) {
                eventOpts.add(jsonEvent.getString("event_name"));
            }
        } catch (JSONException e) {
            eventOpts = null;
            e.printStackTrace();
        }

        List<String> eventFunctionalityOpts = new ArrayList<String>();
        eventFunctionalityOpts.add("Create event");
        eventFunctionalityOpts.add("Join event");
        eventFunctionalityOpts.add("Delete event");
        eventFunctionalityOpts.add("View eventSignUps");
        // eventFunctionalityOpts.add("View map");

        listDataChild.put(listDataHeader.get(0), userOpts); // Header, Child data
        listDataChild.put(listDataHeader.get(1), equipOpts);
        if (eventOpts != null) listDataChild.put(listDataHeader.get(2), eventOpts);
        listDataChild.put(listDataHeader.get(3), eventFunctionalityOpts);
    }
}