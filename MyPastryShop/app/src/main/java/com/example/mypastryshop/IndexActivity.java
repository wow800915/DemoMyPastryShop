package com.example.mypastryshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    ImageButton imageButtonCake;
    private TextView textViewTemp,textViewDrink;
    private ImageView imageViewDrink;
    //A下面到
    private Context context;
    private int[] pictureNumber={R.drawable.img_1,R.drawable.img_2,R.drawable.img_3,R.drawable.img_4,
            R.drawable.img_5,R.drawable.img_6,R.drawable.img_7,R.drawable.img_8,R.drawable.img_9,R.drawable.img_10
    };
    private List<Map<String,Object>> pictureList;
    private RecyclerView recycleViewPicture;
    private ImageView imageViewMain;
    //-A  RECYCLE VIEW的
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        imageButtonCake =findViewById(R.id.imageButton_cake);
        textViewTemp =findViewById(R.id.textView_temp);
        textViewDrink=findViewById(R.id.textView_drink);
        imageViewDrink=findViewById(R.id.imageView_drink);
        //A下面到
        context = this;
        pictureList = new ArrayList<>();
        for(int i =0;i<pictureNumber.length;i++){
            Map<String, Object> data = new HashMap<String,Object>();
            data.put("image",pictureNumber[i]);
            pictureList.add(data);
        }//可以去查看嗎??

        recycleViewPicture = (RecyclerView) findViewById(R.id.recyclerView_picture);
        LinearLayoutManager myLayoutManager_Linear = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recycleViewPicture.setLayoutManager(myLayoutManager_Linear);

        imageViewMain = (ImageView)findViewById(R.id.imageView_main);
        imageViewMain.setVisibility(View.INVISIBLE);

        LinearAdapter myLinearAdapter = new LinearAdapter(context, pictureList,imageViewMain);
        recycleViewPicture.setAdapter(myLinearAdapter);
        //-A  RECYCLE VIEW的

        imageButtonCake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(IndexActivity.this, cakeActivity.class);
                IndexActivity.this.startActivity(go);
            }
        });
    //下面的navigation的Fragment設定part1 記得先設menu跟layout
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnNavigationItemSelectedListener(this);
//        bottomNav.setSelectedItemId(R.id.home_nav);

        new MyAsyncTask().execute();

    }
    //右上角的toolbar設定part1,記得先設menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upper_toolbar_menu,menu);
        return true;
    }
    //右上角的toolbar設定part2
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_message:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,chat).commit();
//                Log.d("menu","右上角的toolbar測試");
                Intent go1 = new Intent(IndexActivity.this, InfoActivity.class);
                IndexActivity.this.startActivity(go1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //下面的navigation的Fragment設定part2
    HomeFragment home = new HomeFragment();
    ShoppingCartFragment shoppingCart = new ShoppingCartFragment();
    PersonalFragment personal =new PersonalFragment();
    ChatFragment chat =new ChatFragment();
    DoPay doPay=new DoPay();

    //下面的navigation的Fragment設定part3
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {//實作了BottomNavigationView.OnNavigationItemSelectedListener的方法
        switch (item.getItemId()) {
            case R.id.home_nav://id在menu內的bottom_nav_menu找的到
                Log.d("menu","點選裡面前");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
                Log.d("menu","點選裡面後");
                return true;
            case R.id.shop_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, shoppingCart).commit();
                return true;
            case R.id.chat_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, chat).commit();
                 return true;
            case R.id.person_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, personal).commit();
                return true;

        }


        return false;
    }
    //上網抓溫度資訊 part1
    private String getWeatherInfo(String urlStr){

        try {
            URL url = new URL(urlStr);
            URLConnection conn =url.openConnection();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine())!=null){
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "發生 IOException 例外";
        }
    }
    //上網抓溫度資訊 part2
    private class MyAsyncTask extends AsyncTask<String, Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            String jsonStr = getWeatherInfo("https://api.openweathermap.org/data/2.5/weather?q=Taipei,tw&appid=ef4c8e7b30cd43a8a72218c86fb3bc35");
//            String jsonStr = getWeatherInfo("http:/192.168.59.119:8080/WeatherApp/Weather");

            Log.d("Weather",jsonStr);
            return jsonStr;
        }
        @Override
        protected void onPostExecute(String result){
            try {
                JSONObject jobj = new JSONObject(result);
                JSONObject mainJobj = jobj.getJSONObject("main");
                double temp = mainJobj.getDouble("temp");
                textViewTemp.setText("(今天溫度:"+(int)(temp-273.15)+"度,我們為您推薦:)");

                if((temp-273.15)>=20){
                    textViewDrink.setText("冰珍珠奶茶");
                    imageViewDrink.setImageResource(R.drawable.bubbletea);
                }else if((temp-273.15)>=20){
                    textViewDrink.setText("熱咖啡");
                    imageViewDrink.setImageResource(R.drawable.hotcoffee);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}