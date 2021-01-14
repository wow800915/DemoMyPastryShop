package com.example.mypastryshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PersonalForAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_for_admin);

        final TextView text_view = (TextView) findViewById(R.id.textView_personalInfo_admin);

        new Thread(new Runnable(){
            @Override
            public void run(){
                MysqlCon con = new MysqlCon();
                con.run();
                final String data = con.getData();
                Log.v("DB",data);
                text_view.post(new Runnable() {
                    public void run() {
                        text_view.setText(data);
                    }
                });
            }
        }).start();
    }
}