package com.example.meet.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.meet.R;

public class ModelActivity extends AppCompatActivity {
private TextView ModleNight;
private TextView ModleDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        ModleNight=findViewById(R.id.ModelNight);
        ModleDay=findViewById(R.id.ModelLight);
        ModleNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                 Intent intent=new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);

            }
        });
        ModleDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                Intent intent=new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }



}
