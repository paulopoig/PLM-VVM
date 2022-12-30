package com.example.plm_vvm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.plm_vvm.activities.HomeActivity;
import com.example.plm_vvm.activities.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    public static final String PREFERENCES  = "prefKey";
    SharedPreferences sharedPreferences;
    public static final String isLogIn = "islogin";

    // Create simple project???
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        boolean bol = sharedPreferences.getBoolean(isLogIn, false);

        new Handler().postDelayed(()->{
            if(bol)
            {
                startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                finish();
            }
            else
            {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                finish();
            }
        }, 3000);
    }
}