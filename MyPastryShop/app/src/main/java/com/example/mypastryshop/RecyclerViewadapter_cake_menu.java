package com.example.mypastryshop;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.util.List;

public class RecyclerViewadapter_cake_menu extends RecyclerView.Adapter<RecyclerViewadapter_cake_menu.MyViewHolder> {
    private Context mContext;
    private List<cake_menu_class> mData;
    private String num = " ";

    public RecyclerViewadapter_cake_menu(Context mContext, List<cake_menu_class> mData) {
        this.mContext = mContext;
        this.mData = mData;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.activity_cake_item, parent, false);
        return new RecyclerViewadapter_cake_menu.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.lin_menu_name.setText(mData.get(position).getMenu_name());
        holder.lin_menu_price.setText(mData.get(position).getMenu_price());
        holder.lin_menu_picture.setImageResource(mData.get(position).getMenu_picture());
        if (mData.get(position).getMenu_name().equals("麥芽長條蛋糕") || mData.get(position).getMenu_name().equals("黑森林長條蛋糕") || mData.get(position).getMenu_name().equals("蜂蜜長條蛋糕") || mData.get(position).getMenu_name().equals("浪漫雪蜜香長條蛋糕") || mData.get(position).getMenu_name().equals("虎皮咖啡長條蛋糕") || mData.get(position).getMenu_name().equals("素鹹蛋糕") || mData.get(position).getMenu_name().equals("6吋戚風蛋糕") || mData.get(position).getMenu_name().equals("精緻濃郁黑魔豆盆栽蛋糕") || mData.get(position).getMenu_name().equals("香濃芋泥雙餡鮮奶蛋糕")) {
            holder.ele_num_btn.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    num = mData.get(position).getMenu_name();
                    num += "_" + newValue;
                    Log.d("p1", num);

                    if (mData.get(position).getMenu_name().equals("麥芽長條蛋糕")) {
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("order_menu", mContext.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status1", num);
                        editor.putInt("status1$",150*newValue);
                        editor.apply();
//                        Log.d("main","麥"+newValue);

                    } else if (mData.get(position).getMenu_name().equals("黑森林長條蛋糕")) {
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("order_menu", mContext.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status2", num);
                        editor.putInt("status2$",180*newValue);
                        editor.apply();
                        Log.d("main","黑"+newValue);
                    } else if (mData.get(position).getMenu_name().equals("蜂蜜長條蛋糕")) {
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("order_menu", mContext.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status3", num);
                        editor.putInt("status3$",179*newValue);
                        editor.apply();
                        Log.d("main","蜂"+num+newValue);
                    } else if (mData.get(position).getMenu_name().equals("浪漫雪蜜香長條蛋糕")) {
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("order_menu", mContext.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status4", num);
                        editor.putInt("status4$",139*newValue);
                        editor.apply();
                    } else if (mData.get(position).getMenu_name().equals("虎皮咖啡長條蛋糕")) {
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("order_menu", mContext.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status5", num);
                        editor.putInt("status5$",229*newValue);
                        editor.apply();
                    } else if (mData.get(position).getMenu_name().equals("素鹹蛋糕")) {
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("order_menu", mContext.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status6", num);
                        editor.putInt("status6$",189*newValue);
                        editor.apply();
                    } else if (mData.get(position).getMenu_name().equals("6吋戚風蛋糕")) {
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("order_menu", mContext.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status7", num);
                        editor.putInt("status7$",159*newValue);
                        editor.apply();
                    } else if (mData.get(position).getMenu_name().equals("精緻濃郁黑魔豆盆栽蛋糕")) {
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("order_menu", mContext.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status8", num);
                        editor.putInt("status8$",230*newValue);
                        editor.apply();
                    } else if (mData.get(position).getMenu_name().equals("香濃芋泥雙餡鮮奶蛋糕")) {
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("order_menu", mContext.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status9", num);
                        editor.putInt("status9$",170*newValue);
                        editor.apply();

                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lin_menu_name;
        TextView lin_menu_price;
        ImageView lin_menu_picture;
        LinearLayout cv_flower;
        ElegantNumberButton ele_num_btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            lin_menu_name = itemView.findViewById(R.id.cb_meal_name);
            lin_menu_price = itemView.findViewById(R.id.tv_meal_price);
            lin_menu_picture =itemView.findViewById(R.id.img_meal_picture);
            cv_flower = itemView.findViewById(R.id.lin_floewr);
            ele_num_btn = itemView.findViewById(R.id.ele_num_btn);
        }
    }
}
