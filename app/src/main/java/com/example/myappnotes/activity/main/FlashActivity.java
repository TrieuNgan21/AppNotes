package com.example.myappnotes.activity.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myappnotes.R;

public class FlashActivity extends AppCompatActivity {

    com.example.myappnotes.SessionManager sessionManager ;

    String getId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("PREPS",0);
                String password = preferences.getString("password","0");

                if (password.equals("0")){
                    Intent intent = new Intent(getApplicationContext(),CreatePasswordActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    Intent intent = new Intent(getApplicationContext(),InputPasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },5000);
    }
}
