package com.example.mypastryshop;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NETWORK_STATS_SERVICE;

public class ShoppingCartFragment extends Fragment {

    private TextView textView;
    private Button buttonCancel,buttonOrder;
    private FirebaseAuth authControl;
    private FirebaseDatabase fbControl;
    private DatabaseReference classDB;
    private String addressData;
    private String order;
    int total;


    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shoppingcart, container, false);
        textView = root.findViewById(R.id.textView_test);

        authControl = FirebaseAuth.getInstance();
        fbControl = FirebaseDatabase.getInstance();
        Log.d("main", "fbControl = " + fbControl);
        classDB = fbControl.getReference("member");
        Log.d("main", "classDB = " + classDB);

        order = "";

                for (int i = 1; i <= 14; i++) {
                    if (this.getActivity().getSharedPreferences("order_menu", MODE_PRIVATE).getString("status" + i, "") != "") {
                        order += this.getActivity().getSharedPreferences("order_menu", MODE_PRIVATE).getString("status" + i, "")
//                                .replace("_", " x ")
                                        + "     (NT$"
                                        + this.getActivity().getSharedPreferences("order_menu", MODE_PRIVATE).getInt("status" + i + "$", 0)
                                +")" + "\n";
                    }
                    Log.d("main", "回圈內"+order);
                }
        for (int i = 1; i <= 14; i++) {
            total += this.getActivity().getSharedPreferences("order_menu", MODE_PRIVATE)
                    .getInt("status" + i + "$", 0);
        }
                textView.setText(order+"\n"+"總共:"+total+"元");
                Log.d("main", order);

        buttonCancel=root.findViewById(R.id.button_cancel);
        Log.d("main","找我id");

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref
                        = ShoppingCartFragment.this.getActivity().getSharedPreferences("order_menu", MODE_PRIVATE);
                pref.edit().clear().commit();
                textView.setText("");
            }
        });
        Log.d("main","authControl.getUid()"+authControl.getUid());

//        classDB.child(authControl.getUid()).child("Address").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                addressData = dataSnapshot.getValue().toString();
////                Log.d("main","地址="+ addressData);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });

        buttonOrder=root.findViewById(R.id.button_order);
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("確定將訂單設定完成了嗎?");


                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Date date = new Date();
//                        TimeZone tz = TimeZone.getTimeZone("Asia/Taiwan");
                        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
                        SimpleDateFormat bartDateFormat2 = new SimpleDateFormat("HHmmss");
                        String today = bartDateFormat.format(date);
                        String time = bartDateFormat2.format(date);
                        classDB.child("13OFtYenFnS2o7GUaFRoYjtnqgl1").child("order").child(authControl.getUid()).child(today+time).child("content").setValue(order);
                        classDB.child("13OFtYenFnS2o7GUaFRoYjtnqgl1").child("order").child(authControl.getUid()).child(today+time).child("total").setValue(total);
                        classDB.child("13OFtYenFnS2o7GUaFRoYjtnqgl1").child("order").child(authControl.getUid()).child(today+time).child("time").setValue(today);
                        classDB.child("13OFtYenFnS2o7GUaFRoYjtnqgl1").child("order").child(authControl.getUid()).child(today+time).child("statue").setValue("尚未出貨");
                        SharedPreferences pref
                                = ShoppingCartFragment.this.getActivity().getSharedPreferences("order_menu", MODE_PRIVATE);
                        pref.edit().clear().commit();
                        textView.setText("");

                        DoPay doPay=new DoPay();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, doPay).commit();

                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        //取得系統時間
                        SharedPreferences pref
                                = ShoppingCartFragment.this.getActivity().getSharedPreferences("order_menu", MODE_PRIVATE);
                        pref.edit().clear().commit();
                        textView.setText("");
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return root;
    }

}