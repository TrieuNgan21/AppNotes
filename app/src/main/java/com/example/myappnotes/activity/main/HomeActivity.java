package com.example.myappnotes.activity.main;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myappnotes.R;

public class HomeActivity extends AppCompatActivity {

    int Background_int;
    String Background_gallery;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_home);
        loadBackground();
    }

    public void loadBackground()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

        Background_int = sharedPreferences.getInt("Background_int", R.drawable.background1);
        Background_gallery = sharedPreferences.getString("Background_gallery", "");

    }
}
