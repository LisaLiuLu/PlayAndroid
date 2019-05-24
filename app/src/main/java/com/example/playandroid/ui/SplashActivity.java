package com.example.playandroid.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.playandroid.MainActivity;

/**
 *启动界面
 */
public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                go2MainPage();
            }
        },3000);
    }
    private void go2MainPage(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
