package com.example.mypastryshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PersonalForCustomerActivity extends AppCompatActivity {

    private TextView text_view;
    private String name;
    private Button buttonPFC;
    private EditText editTextAddress,editTextName, editTextBirthday,editTextPhone;
    private Context context;
    private FirebaseAuth authControl;
    private FirebaseDatabase fbControl;
    private DatabaseReference classDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_for_customer);
        text_view=findViewById(R.id.textView14);

        SharedPreferences sp = this.getSharedPreferences("user", 0);
        name=sp.getString("name", "");
//        account=sp.getString("account", "");

        final TextView text_view = (TextView) findViewById(R.id.textView_personalInfo);
        buttonPFC=findViewById(R.id.button_PFC);
        editTextName=findViewById(R.id.editText_name_pfc);
        editTextBirthday =findViewById(R.id.editText_birthday_pfc);
        editTextPhone=findViewById(R.id.editText_phone_pfc);
        editTextAddress=findViewById(R.id.editText_address_pfc);
        context=this;

        authControl = FirebaseAuth.getInstance();
        fbControl = FirebaseDatabase.getInstance();
        Log.d("main", "fbControl = " + fbControl);
        classDB = fbControl.getReference("member");
        Log.d("main", "classDB = " + classDB);


        new Thread(new Runnable(){
            @Override
            public void run(){
                MysqlCon con = new MysqlCon();
                con.run();
                final String data = con.getOneData(name);
                Log.v("DB",data);
                text_view.post(new Runnable() {
                    public void run() {
                        text_view.setText(data);
                    }
                });
            }
        }).start();

        buttonPFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextName.getText().toString().equals("")||editTextBirthday.getText().toString().equals("")||editTextPhone.getText().toString().equals("")||editTextAddress.getText().toString().equals("")){
                    Toast.makeText(context,"請輸入完整更新資料",Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MysqlCon con = new MysqlCon();
                            con.run();
                            final String a = con.updateOneData(name, editTextName.getText().toString(), editTextBirthday.getText().toString(), editTextPhone.getText().toString(), editTextAddress.getText().toString());
                            text_view.post(new Runnable() {
                                public void run() {
                                }
                            });
                            final String data = con.getOneData(name);
                            Log.v("DB", data);
                            text_view.post(new Runnable() {
                                public void run() {
                                    text_view.setText(data);
                                }
                            });
                            FirebaseUser user=authControl.getCurrentUser();
                            classDB.child(user.getUid()).child("Address").setValue(editTextAddress.getText().toString());
                            editTextPhone.setText("");
                            editTextName.setText("");
                            editTextAddress.setText("");
                            editTextBirthday.setText("");
                        }
                    }).start();
                }
            }
        });

    }
}