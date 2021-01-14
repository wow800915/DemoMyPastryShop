package com.example.mypastryshop;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChatFragment extends Fragment {
    View root;
    Button chatButton;
    ScrollView chatScroView;
    LinearLayout chatlinearlayout;
    EditText bodytext,userText;
    String name,account;
    FirebaseDatabase database;
    DatabaseReference myRef,youRef;
//    HashMap userHashMap;
    int i=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        root=inflater.inflate(R.layout.fragment_chat, container, false);
        chatButton= root.findViewById(R.id.button_chat);
        chatScroView=root.findViewById(R.id.scrollView_chat);
        chatlinearlayout=root.findViewById(R.id.linearlayout_chat);
        bodytext=root.findViewById(R.id.editView_chatbody);
        userText=root.findViewById(R.id.userText);
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

////////////////////////////取出帳號資料
        SharedPreferences sp = this.getActivity().getSharedPreferences("user", 0);
        name=sp.getString("name", "");
        account=sp.getString("account", "");

//        if(!(account.equals("13OFtYenFnS2o7GUaFRoYjtnqgl1"))) {
            userText.setVisibility(View.GONE);
//        }

        database= FirebaseDatabase.getInstance();
//        myRef = database.getReference("/member/"+authControl.getUid()+"/message");
        myRef = database.getReference("/member/"+account+"/message");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //  userHashMap = (HashMap) dataSnapshot.getValue();
                // Log.e("t1",userHashMap.get("message").toString());

                if(i==0){
                    charcard ("如有任何問題歡迎留言,服務專員會盡快為您服務",true);
                    i++;
                }else{
                    try {
                        charcard(dataSnapshot.getValue().toString(), false);
                    }catch (Exception e){

                    }

                }

                //Log.e("t1",dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                youRef = database.getReference("/member/"+userText.getText().toString()+"/message");
                charcard (bodytext.getText().toString(),true);
                if(account.equals("13OFtYenFnS2o7GUaFRoYjtnqgl1")) {
                    youRef.setValue("優樂芋服務人員" + " say:\n" + bodytext.getText().toString());
                }else{
                    youRef.setValue(account + " say:\n" + bodytext.getText().toString());
                }
                bodytext.setText("");
            }
        });



    }

    void charcard (final String bodyString, boolean right){
        CardView cardView=new CardView(root.getContext());
        TextView tv = new TextView(root.getContext());
        tv.setText(bodyString);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(700,LinearLayout.LayoutParams.WRAP_CONTENT);
        if(right==true){
            lp.gravity=Gravity.RIGHT;
            tv.setBackgroundColor(Color.parseColor("#ffaaff"));
        }
        else{
            lp.gravity=Gravity.LEFT;
            tv.setBackgroundColor(Color.parseColor("#aaaaaa"));
            if(account.equals("13OFtYenFnS2o7GUaFRoYjtnqgl1")) {
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s[] = bodyString.split(" say:");
                        Toast.makeText(root.getContext(), s[0].trim(), Toast.LENGTH_SHORT).show();
                        userText.setText(s[0].trim());
                    }
                });
            }
        }

        cardView.addView(tv);
        cardView.setLayoutParams(lp);//對話框大小
        cardView.setUseCompatPadding(true);//間距
        cardView.setRadius((float) 10.000);//圓弧大小

        chatlinearlayout.addView(cardView);
        //強制往下
        Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                chatScroView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }
}