package com.example.nh612u.gofish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Admin_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        final Button button = (Button) findViewById(R.id.createUser);
        Bundle email = getIntent().getExtras();
        if (email != null) {
            String emailAddr = email.getString("email");
        }
        try {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(Admin_Activity.this, RegisterUserActivity.class));
                }

            });
        } catch(NullPointerException e) {
            Log.wtf("Error:", "Null Pointer Exception thrown");
        }
        final Button deleteButton = (Button) findViewById(R.id.deleteUser);
        try {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(Admin_Activity.this, DeleteActivity.class));
                }

            });
        } catch(NullPointerException e) {
            Log.wtf("Error:", "Null Pointer Exception thrown");
        }
    }
}
