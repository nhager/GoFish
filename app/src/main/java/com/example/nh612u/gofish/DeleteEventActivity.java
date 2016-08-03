package com.example.nh612u.gofish;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteEventActivity extends AppCompatActivity {
    private String event_id_to_delete = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_event);
        getAllEvents();
        setDeleteButtonListener();
    }

    private void getAllEvents() {
        HttpHelper httpHelper = new HttpHelper(getGetAllEventsCallback());
        httpHelper.GET(HttpHelper.TABLE.EVENTS, new JSONObject());
    }

    private void setDeleteButtonListener() {
        final Button deleteButton = (Button) findViewById(R.id.eventDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HttpHelper httpHelper = new HttpHelper(getDeleteEventCallback());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("event_id", event_id_to_delete);
                    httpHelper.DELETE(DeleteEventActivity.this, HttpHelper.TABLE.EVENT,
                            jsonObject);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler.Callback getGetAllEventsCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                if (response == null || response.contains("error") ||
                        response.contains("message")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            response, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    populateListViewWithEvents(response);
                }
                return true;
            }
        };
        return callback;
    }


    private Handler.Callback getDeleteEventCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                if (response == null || response.contains("error") ||
                        response.contains("message")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            response, Toast.LENGTH_SHORT);
                    toast.show();
                }
                finish();
                startActivity(getIntent());
                return true;
            }
        };
        return callback;
    }

    private void populateListViewWithEvents(final String response) {
        final List<JSONObject> rows = new ArrayList<JSONObject>();
        try {
            List<String> itemList = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                final String row = "Name: " + jsonObject.get("event_name").toString();
                itemList.add(row);
                rows.add(jsonObject);
            }
            System.out.println(Arrays.toString(rows.toArray()));
            if (itemList.isEmpty()) {
                TextView text = (TextView) findViewById(R.id.eventDeleteText);
                text.setText("There are no events to delete.");
                return;
            }
            ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(DeleteEventActivity.this,
                    android.R.layout.simple_list_item_1, itemList);
            ListView lvItemList = (ListView) findViewById(R.id.eventDeleteList);
            lvItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        parent.getChildAt(i).setBackgroundColor(Color.WHITE);
                    }
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    try {
                        final JSONObject event = rows.get(position);
                        event_id_to_delete = "";
                        event_id_to_delete += event.get("event_id");
                        TextView tvEventName = (TextView) findViewById(R.id.eventDeleteNameDetail);
                        TextView tvEventOrganizer = (TextView) findViewById(R.id.eventDeleteOrganizerDetail);
                        TextView tvEventDate = (TextView) findViewById(R.id.eventDeleteDateDetail);
                        tvEventName.setText("Name: " + event.get("event_name").toString());
                        tvEventOrganizer.setText("Organizer: " + event.get("event_organizer").toString());
                        tvEventDate.setText("Date: " + event.get("event_date").toString() +
                            " @ " + event.get("event_time").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            lvItemList.setAdapter(itemAdapter);
        } catch (JSONException e) {
            e.printStackTrace();;
        }
    }
}

