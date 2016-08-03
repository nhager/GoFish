package com.example.nh612u.gofish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    Spinner eventSelect;
    Spinner typeSelect;
    ListView listView;

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

        typeSelect = (Spinner) findViewById(R.id.typeFilterSpinner);

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
        listView = (ListView) findViewById(R.id.inventoryList);
        /*
        String[] values = getInventory();

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
        */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0);
            }

        });

        HttpHelper httpHelperType = new HttpHelper(getTypesCallback());
        JSONObject jsonObjectType = new JSONObject();
        httpHelperType.GET(HttpHelper.TABLE.ITEM_TYPE, jsonObjectType);

        HttpHelper httpHelperItems = new HttpHelper(getItemsCallback());
        JSONObject jsonObjectItems = new JSONObject();
        httpHelperItems.GET(HttpHelper.TABLE.ITEMS, jsonObjectItems);
    }
    private Handler.Callback getTypesCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                addTypes(response);
                return true;
            }
        };
        return callback;
    }
    private boolean addTypes(String response) {
        boolean retval = false;
        try {
            Object json = new JSONTokener(response).nextValue();
            List<String> spinnerArray =  new ArrayList<>();
            final Context cur = this;
            if(json instanceof JSONObject){
                JSONObject jsonObj = new JSONObject(response);
                if(jsonObj.has("message") &&
                        jsonObj.get("message").equals("User with provided parameters not found.")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Types not found.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    spinnerArray.add(jsonObj.getString("item_type"));
                }
            } else if (json instanceof JSONArray){
                JSONArray jsonObj = new JSONArray(response);
                for(int i = 0; i < jsonObj.length(); i++){

                    spinnerArray.add(jsonObj.getJSONObject(i).
                            getString("item_type"));
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, spinnerArray);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            typeSelect.setAdapter(dataAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return retval;
        }
    }

    private Handler.Callback getItemsCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                addItems(response);
                return true;
            }
        };
        return callback;
    }
    private boolean addItems(String response) {
        boolean retval = false;
        try {
            Object json = new JSONTokener(response).nextValue();
            List<String> spinnerArray =  new ArrayList<>();
            final Context cur = this;
            if(json instanceof JSONObject){
                JSONObject jsonObj = new JSONObject(response);
                if(jsonObj.has("message") &&
                        jsonObj.get("message").equals("User with provided parameters not found.")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Types not found.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    spinnerArray.add(jsonObj.getString("item_name") + " type: " +
                            jsonObj.getString("item_type") + " " + " \r\nChecked Out: " +
                            jsonObj.getString("userId") == null ? "Y":"N");
                }
            } else if (json instanceof JSONArray){
                JSONArray jsonObj = new JSONArray(response);
                for(int i = 0; i < jsonObj.length(); i++){
                    spinnerArray.add(jsonObj.getJSONObject(i).getString("item_type") + " " +
                            jsonObj.getJSONObject(i).getString("item_name") + " \r\nChecked Out: " +
                            (jsonObj.getJSONObject(i).getString("assigned_user_id") == "" ? "Yes":"No"));
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, spinnerArray);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listView.setAdapter(dataAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return retval;
        }
    }

    //TODO: Get event list from database.
    //TODO: User event object instead of string.
    public String[] getEvents(){
        String[] strs = {"David Purcell","Phillp-a","Blanco"};
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