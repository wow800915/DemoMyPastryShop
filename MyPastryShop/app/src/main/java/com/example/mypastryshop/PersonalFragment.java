package com.example.mypastryshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersonalFragment extends Fragment {
    private FirebaseAuth authControl;
    private StorageReference mStorageRef;
    private ProgressBar progressPer;
    private ImageView imageViewPer;
    private ListView listViewData;
    private ArrayList<Map<String, String>> dataList;
    private SimpleAdapter adapter;
    private FirebaseDatabase fbControl;
    private DatabaseReference classDB;
    private int dataCount;
    private String addressData;
    private TextView textViewAddress;
    private Button buttonPerAccount;
    private Button btLogout;
    private GoogleSignInClient googleSignInClient;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_personal, container, false);

        authControl = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        progressPer = root.findViewById(R.id.progress_per);
        imageViewPer = root.findViewById(R.id.imageView_per);
        textViewAddress = root.findViewById(R.id.textView_location);
        buttonPerAccount = root.findViewById(R.id.button_peraccount);
        btLogout =root.findViewById(R.id.button_signout);

        googleSignInClient = GoogleSignIn.getClient(getContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sign out from google
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            authControl.signOut();
///                           Intent go3 = new Intent(root.getContext(), PersonalForCustomerActivity.class);
//                            root.getContext().startActivity(go3);
                            Toast.makeText(root.getContext(),"登出成功",Toast.LENGTH_SHORT).show();
                            Intent go = new Intent(root.getContext(), LoginActivity.class);
                            root.getContext().startActivity(go);
                        }
                    }
                });

            }
        });

        buttonPerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (authControl.getUid().equals("13OFtYenFnS2o7GUaFRoYjtnqgl1")) {
                    Intent go1 = new Intent(root.getContext(), PersonalForAdminActivity.class);
                    root.getContext().startActivity(go1);
                } else {
                    Intent go2 = new Intent(root.getContext(), PersonalForCustomerActivity.class);
                    root.getContext().startActivity(go2);
                }
            }
        });

        Log.i("下載", "1 開始....");
        StorageReference leaf = mStorageRef.child("pic" + authControl.getUid());
            /*
            Task<byte[]> task = leaf.getBytes(53956726);
            Log.i("下載", "2 開始....");
            task.addOnSuccessListener(
                    new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(final byte[] bytes) {
                            Log.i("下載", "3 開始....");
                            MainActivity.this.runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.i("下載", "4 開始....");
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            imageView.setImageBitmap(bitmap);
                                            Log.i("下載", "成功....");
                                        }

                                    }
                            );
                        }
                    }
            );
            */
        //final File file = new File("/data/data/com.example.clouddatajohnatpuxin123/coffee.jpg");
        File file = null;
        try {
            file = File.createTempFile("pic" + authControl.getUid(), "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final FileDownloadTask task = leaf.getFile(file);
        task.addOnProgressListener(
                new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        //Log.i("下載", "已下載:" + taskSnapshot.getBytesTransferred());
                        final int percent = (int) ((double) taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount() * 100);
                        Log.i("下載", "已下載:" + percent + " %");
                        getActivity().runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        progressPer.setProgress(percent);
                                    }
                                }
                        );

                    }
                }
        );
        final File finalFile = file;
        task.addOnSuccessListener(
                new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(finalFile));
                        imageViewPer.setImageBitmap(bitmap);
                    }
                }
        );
        Log.d("main", "imageViewPer是:" + imageViewPer);
        if (imageViewPer == null) {
            imageViewPer.setImageResource(R.drawable.userpic);
        }


        listViewData = (ListView) root.findViewById(R.id.listView_id);

        dataList = new ArrayList<Map<String, String>>();
        dataList.clear();

        adapter = new SimpleAdapter(getContext(), dataList, R.layout.item_layout, new String[]{"time", "content", "statue"}, new int[]{R.id.textView_time, R.id.textView_content, R.id.textView_statue});

        listViewData.setAdapter(adapter);

        fbControl = FirebaseDatabase.getInstance();
        Log.d("main", "fbControl = " + fbControl);
        classDB = fbControl.getReference("member");
        Log.d("main", "classDB = " + classDB);

        classDB.child(authControl.getUid()).child("Address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    addressData="尚未輸入地址";
                }else {
                    addressData = dataSnapshot.getValue().toString();
                    textViewAddress.setText(addressData);
                    Log.d("main", "地址=" + addressData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        if (authControl.getUid().equals("Ldd9WLz3SIUcsj6TEJchBzsYbT43")) {
            Log.d("DB", "8888");
            Log.d("DB", classDB.child("13OFtYenFnS2o7GUaFRoYjtnqgl1").child("order").getKey());
            classDB.child("13OFtYenFnS2o7GUaFRoYjtnqgl1").child("order").child(authControl.getUid()).addValueEventListener(new ValueEventListener() {

                public void onDataChange(DataSnapshot snapshot) {

                    dataList.clear();
                    dataCount = (int) snapshot.getChildrenCount();
                    Log.d("main", "dataCount = " + dataCount);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, String> mapData = new HashMap<String, String>();

                        String timeValue = (String) ds.child("time").getValue();
                        mapData.put("time", timeValue);

                        String contentValue = (String) ds.child("content").getValue();
                        mapData.put("content", contentValue);

                        String statueValue = (String) ds.child("statue").getValue();
                        mapData.put("statue", statueValue);

                        dataList.add(mapData);
                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            classDB.child("13OFtYenFnS2o7GUaFRoYjtnqgl1").child("order").child(authControl.getUid()).addValueEventListener(new ValueEventListener() {

                public void onDataChange(DataSnapshot snapshot) {

                    dataList.clear();
                    dataCount = (int) snapshot.getChildrenCount();
                    Log.d("main", "dataCount = " + dataCount);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, String> mapData = new HashMap<String, String>();

                        String timeValue = (String) ds.child("time").getValue();
                        mapData.put("time", timeValue);

                        String contentValue = (String) ds.child("content").getValue();
                        mapData.put("content", contentValue);

                        String statueValue = (String) ds.child("statue").getValue();
                        mapData.put("statue", statueValue);

                        dataList.add(mapData);
                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        return root;
    }
}
