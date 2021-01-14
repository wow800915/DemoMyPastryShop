package com.example.mypastryshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    //// Handler to handle wait and go
    Handler handler;

    //// below is the class which run in Handller
    class change_activity implements Runnable {


        @Override
        public void run() {

            Intent go = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(go);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onResume() {
        super.onResume();

        //// below modified layout structure 控制全螢幕
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN
        );
        //// below wait for 1.5 second then change to login
        handler = new Handler();
        change_activity change = new change_activity();
        handler.postDelayed(change, 3000);
    }

}