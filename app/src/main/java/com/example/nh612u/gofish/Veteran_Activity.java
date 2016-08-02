package com.example.nh612u.gofish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Veteran_Activity extends AppCompatActivity {
    Button add;
    Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veteran);
        add = (Button) findViewById(R.id.addEC);
        delete = (Button) findViewById(R.id.deleteEC);
        setAddOnClick();
        setDeleteOnClick();
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
                    startActivity(intent);
                }

            }
        });
    }
}
