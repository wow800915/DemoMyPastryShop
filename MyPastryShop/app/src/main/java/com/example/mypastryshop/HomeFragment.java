package com.example.mypastryshop;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class HomeFragment extends Fragment {

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

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        imageButtonCake =root.findViewById(R.id.imageButton_cake);
        textViewTemp =root.findViewById(R.id.textView_temp);
        textViewDrink=root.findViewById(R.id.textView_drink);
        imageViewDrink=root.findViewById(R.id.imageView_drink);
        //A下面到
        context = getContext();
        pictureList = new ArrayList<>();
        for(int i =0;i<pictureNumber.length;i++){
            Map<String, Object> data = new HashMap<String,Object>();
            data.put("image",pictureNumber[i]);
            pictureList.add(data);
        }//可以去查看嗎??

        recycleViewPicture = root.findViewById(R.id.recyclerView_picture);
        LinearLayoutManager myLayoutManager_Linear = new LinearLayoutManager(root.getContext(),LinearLayoutManager.HORIZONTAL,false);
        recycleViewPicture.setLayoutManager(myLayoutManager_Linear);

        imageViewMain = root.findViewById(R.id.imageView_main);
        imageViewMain.setVisibility(View.INVISIBLE);

        LinearAdapter myLinearAdapter = new LinearAdapter(root.getContext(), pictureList,imageViewMain);
        recycleViewPicture.setAdapter(myLinearAdapter);
        //-A  RECYCLE VIEW的


        imageButtonCake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(root.getContext(), cakeActivity.class);
                root.getContext().startActivity(go);
            }
        });
        new MyAsyncTask().execute();
        return root;
    }//上網抓溫度資訊 part1
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
