package com.example.nh612u.gofish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Admin_Activity extends AppCompatActivity {

    String email;
    String id;
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        final Button button = (Button) findViewById(R.id.createUser);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            email = b.getString("email");
            id = b.getString("id");
            role = b.getString("role");
        }
        try {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), RegisterUserActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("id", id);
                    intent.putExtra("role", role);
                    startActivity(intent);
                }

            });
        } catch(NullPointerException e) {
            Log.wtf("Error:", "Null Pointer Exception thrown");
        }
        final Button deleteButton = (Button) findViewById(R.id.deleteUser);
        try {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), DeleteActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("id", id);
                    intent.putExtra("role", role);
                    startActivity(intent);
                }

            });
        } catch(NullPointerException e) {
            Log.wtf("Error:", "Null Pointer Exception thrown");
        }

        // Jon event stuff
        final Button eventAddButton = (Button) findViewById(R.id.createEvent);
        try {
            eventAddButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), CreateEventActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("id", id);
                    intent.putExtra("role", role);
                    startActivity(intent);
                }

            });
        } catch(NullPointerException e) {
            Log.wtf("Error:", "Null Pointer Exception thrown");
        }

        final Button eventDeleteButton = (Button) findViewById(R.id.deleteEvent);
        try {
            eventDeleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), DeleteEventActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("id", id);
                    intent.putExtra("role", role);
                    startActivity(intent);
                }

            });
        } catch(NullPointerException e) {
            Log.wtf("Error:", "Null Pointer Exception thrown");
        }

        final Button eventViewButton = (Button) findViewById(R.id.viewEvent);
        try {
            eventViewButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), ViewEvents.class);
                    intent.putExtra("email", email);
                    intent.putExtra("id", id);
                    intent.putExtra("role", role);
                    startActivity(intent);
                }

            });
        } catch(NullPointerException e) {
            Log.wtf("Error:", "Null Pointer Exception thrown");
        }

        final Button eventJoinButton = (Button) findViewById(R.id.joinEvent);
        try {
            eventJoinButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(),JoinEvent.class);
                    intent.putExtra("email", email);
                    intent.putExtra("id", id);
                    intent.putExtra("role", role);
                    startActivity(intent);
                }

            });
        } catch(NullPointerException e) {
            Log.wtf("Error:", "Null Pointer Exception thrown");
        }

        final Button mapButton = (Button) findViewById(R.id.viewMap);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MapMainActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("id", id);
                intent.putExtra("role", role);
                startActivity(intent);
            }
        });

        final Button inventoryButton = (Button) findViewById(R.id.inventoryButton);
        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), InventoryActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("id", id);
                intent.putExtra("role", role);
                startActivity(intent);
            }
        });
    }
}
