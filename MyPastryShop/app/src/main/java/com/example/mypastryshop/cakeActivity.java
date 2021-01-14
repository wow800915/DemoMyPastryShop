package com.example.mypastryshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class cakeActivity extends AppCompatActivity {
        private List<cake_menu_class> lstmeal;
        private TextView tv_resname;
        private Button btn_order_meal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_shopping);
        tv_resname = findViewById(R.id.textView_cakeName);
        btn_order_meal = findViewById(R.id.button_orderCake);
        getres_name();
        setintent();
        RecyclerView Italyres_menu = findViewById(R.id.recyclerView_menu);
        setLstmeal();
        RecyclerViewadapter_cake_menu myAdapter = new RecyclerViewadapter_cake_menu(cakeActivity.this, lstmeal);
        Italyres_menu.setLayoutManager(new LinearLayoutManager(cakeActivity.this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        Italyres_menu.addItemDecoration(dividerItemDecoration);
        Italyres_menu.setAdapter(myAdapter);
    }

    private void setLstmeal() {
        lstmeal = new ArrayList<>();
        lstmeal.add(new cake_menu_class("麥芽長條蛋糕", "$150",R.drawable.i010001_1573436694));
        lstmeal.add(new cake_menu_class("黑森林長條蛋糕", "$180",R.drawable.i010001_1573438003));
        lstmeal.add(new cake_menu_class("蜂蜜長條蛋糕", "$179",R.drawable.i010001_1573438920));
        lstmeal.add(new cake_menu_class("浪漫雪蜜香長條蛋糕", "$139",R.drawable.i010001_1573438921));
        lstmeal.add(new cake_menu_class("虎皮咖啡長條蛋糕", "$229",R.drawable.i010001_1573440062));
        lstmeal.add(new cake_menu_class("素鹹蛋糕", "$189",R.drawable.i010001_1573443138));
        lstmeal.add(new cake_menu_class("6吋戚風蛋糕", "$159",R.drawable.i010001_1597290215));
        lstmeal.add(new cake_menu_class("精緻濃郁黑魔豆盆栽蛋糕", "$230",R.drawable.i010003_1496807739));
        lstmeal.add(new cake_menu_class("香濃芋泥雙餡鮮奶蛋糕", "$170",R.drawable.i010005_1580873198));

    }

    private void getres_name() {
        tv_resname.setText(getSharedPreferences("italy_res", MODE_PRIVATE).getString("res_name", ""));
    }

    private void setintent() {
        btn_order_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(cakeActivity.this,"已經加入購物車",Toast.LENGTH_LONG).show();
                finish();
            }
        });


    }


//
}
