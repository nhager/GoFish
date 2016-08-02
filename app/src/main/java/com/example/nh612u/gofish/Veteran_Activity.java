package com.example.nh612u.gofish;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Veteran_Activity extends AppCompatActivity {
    Button add;
    Button delete;
    Button view;
    Button join;
    Button map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veteran);
        add = (Button) findViewById(R.id.addEC);
        delete = (Button) findViewById(R.id.deleteEC);
        view = (Button) findViewById(R.id.viewEventsVet);
        join = (Button) findViewById(R.id.joinEventsVet);
        map = (Button) findViewById(R.id.addMapVet);
        setAddOnClick();
        setDeleteOnClick();
        setViewOnClick();
        setJoinEventsOnClick();
        setAddMap();
    }
    private void setAddOnClick() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    Intent intent = new Intent(getBaseContext(), AddEC.class);
                    intent.putExtra("email", bundle.getString("email"));
                    intent.putExtra("id", bundle.getString("id"));
                    intent.putExtra("role", bundle.getString("role"));
                    startActivity(intent);
                }
            }
        });
    }
    private void setDeleteOnClick() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    Intent intent = new Intent(getBaseContext(), DeleteEC.class);
                    intent.putExtra("email", bundle.getString("email"));
                    intent.putExtra("id", bundle.getString("id"));
                    intent.putExtra("role", bundle.getString("role"));
                    startActivity(intent);
                }

            }
        });
    }
    private void setViewOnClick() {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    Intent intent = new Intent(getBaseContext(), ViewEvents.class);
                    intent.putExtra("email", bundle.getString("email"));
                    intent.putExtra("id", bundle.getString("id"));
                    intent.putExtra("role", bundle.getString("role"));
                    startActivity(intent);
                }

            }
        });
    }
    private void setJoinEventsOnClick() {
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    Intent intent = new Intent(getBaseContext(), JoinEvent.class);
                    intent.putExtra("email", bundle.getString("email"));
                    intent.putExtra("id", bundle.getString("id"));
                    intent.putExtra("role", bundle.getString("role"));
                    startActivity(intent);
                }

            }
        });
    }
    private void setAddMap() {
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    Intent intent = new Intent(getBaseContext(), MapAddMarkerActivity.class);
                    intent.putExtra("email", bundle.getString("email"));
                    intent.putExtra("id", bundle.getString("id"));
                    intent.putExtra("role", bundle.getString("role"));
                    startActivity(intent);
                }

            }
        });
    }
}
