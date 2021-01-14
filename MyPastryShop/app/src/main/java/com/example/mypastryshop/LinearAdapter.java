package com.example.mypastryshop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.ViewHolder> {
    private final List<Map<String, Object>> myPictureList;
    private final LayoutInflater myLayoutInflater;
    private final Context myContext;
    private final ImageView myImageViewMain;

    public LinearAdapter(Context context, List<Map<String, Object>> pictureList,ImageView imageViewMain) {
        myPictureList = pictureList;
        myLayoutInflater = LayoutInflater.from(context);
        myContext = context;
        myImageViewMain = imageViewMain;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = myLayoutInflater.inflate(R.layout.picture_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LinearAdapter.ViewHolder holder, int position) {
        Map<String,Object> data = myPictureList.get(position);//*******1次拿一個?*******不用跑回歸嗎*********************
        int number =(int) data.get("image");
        holder.imageView_picID.setImageResource(number);
        holder.imageView_picID.setTag(position);
    }

    @Override
    public int getItemCount() {
        return myPictureList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private final ImageView imageView_picID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView_picID = (ImageView) itemView.findViewById(R.id.imageView_picture);

            imageView_picID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myImageViewMain.setVisibility(View.VISIBLE);
                    int index =(int) imageView_picID.getTag();
                    Log.d("main","index ="+index);
                    Map<String, Object> data = myPictureList.get(index);
                    int number = (int) data.get("image");
                    myImageViewMain.setImageResource(number);

                }
            });
        }
    }
}
