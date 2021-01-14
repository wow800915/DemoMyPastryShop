package com.example.mypastryshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private Button buttonSignUp, buttonTake;
    private Switch switchPass;
    private EditText editTextEmailReg, editTextPasswordReg, editTextAddressReg;
    private Context context;
    private String email, password, address;
    private FirebaseAuth authControl;
    private FirebaseDatabase fbControl;
    private DatabaseReference classDB;
    private StorageReference mStorageRef;
    private ImageView imageViewPicReg;
    private TextView textView3;
    private String imagepath=null;
    private static final int GALLERY_REQUEST_CODE=123;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;

    Bitmap theImage;
    Bitmap testPic;
    ProgressBar busy, progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        buttonSignUp = (Button) findViewById(R.id.button_signUp);
        editTextEmailReg = (EditText) findViewById(R.id.editText_email_reg);
        editTextPasswordReg = (EditText) findViewById(R.id.editText_password_reg);
        editTextAddressReg = (EditText) findViewById(R.id.editText_address_reg);
        imageViewPicReg = (ImageView) findViewById(R.id.imageView_pic_reg);
        buttonTake = (Button) findViewById(R.id.button_take);
        busy = this.findViewById(R.id.busy);
        progress = this.findViewById(R.id.progress);
        textView3 = this.findViewById(R.id.textView3);


        context = this;
        authControl = FirebaseAuth.getInstance();
        fbControl = FirebaseDatabase.getInstance();
        Log.d("main", "fbControl = " + fbControl);
        classDB = fbControl.getReference("member");
        Log.d("main", "classDB = " + classDB);

        busy.setVisibility(View.INVISIBLE);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        buttonTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("選擇拍照");


                builder.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {//同意不同意,授權使用照相的功能
                            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.CAMERA,}, MY_CAMERA_PERMISSION_CODE);
                        } else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            RegisterActivity.this.startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    }
                });

                builder.setNegativeButton("從相簿", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Intent intent = new Intent();
                        intent.setType("image/'");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"pick an image"),GALLERY_REQUEST_CODE);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextEmailReg.length() == 0 || editTextPasswordReg.length() == 0) {
                    Toast.makeText(context, "please input your email & password", Toast.LENGTH_SHORT).show();
                } else {
                    email = editTextEmailReg.getText().toString();
                    password = editTextPasswordReg.getText().toString();
                    address = editTextAddressReg.getText().toString();

                    authControl.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                MysqlCon con = new MysqlCon(editTextEmailReg.getText().toString(), editTextPasswordReg.getText().toString(), editTextAddressReg.getText().toString());
                                Thread a = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        con.run();
                                        final String data = con.insertData();
                                        Log.v("DB", data);
                                        textView3.post(new Runnable() {
                                            public void run() {
                                            }
                                        });

                                    }
                                });
                                a.start();

                                try {
                                    a.join();
                                } catch (InterruptedException e) {
                                    System.out.println("執行緒被中斷");
                                }

                                Log.d("main", "創立成功");


                                FirebaseUser user = authControl.getCurrentUser();
                                classDB.child(user.getUid()).child("Email").setValue(email);
                                classDB.child(user.getUid()).child("Address").setValue(address);
                                classDB.child(user.getUid()).child("message").setValue("如有任何問題歡迎留言,服務專員會盡快為您服務");
                                Toast.makeText(context, "Register Ok", Toast.LENGTH_LONG).show();

                                busy.setVisibility(View.VISIBLE);
                                //將ImageView 中的圖片化為  byte 陣列

                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                //Bitmap物件有一個函數compress,可以對圖片進行壓縮並儲存到ByteArrayOutputStream物件中
                                byte[] blob;//ContentValues 需要放入圖片,但必須是byte[]形式

                                //抓取圖片:這裡從資源檔Drawable處抓出
//                                    Drawable raw = imageViewPicReg.getDrawable();
//                                Drawable raw = RegisterActivity.this.getResources().getDrawable(R.drawable.mb10);
                                    //Drawable型態要轉出成byte[],中間需要Bitmap
//                                    Bitmap bitmap = ((BitmapDrawable) raw).getBitmap(); //先將Drawable轉型成BitmapDrawable
                                //接著就可以呼叫BitmapDrawable的getBitmap()
                                //如此可得到Bitmap
                                //Bitmap也無法直接轉成byte[]!必須先存到一段特殊的記憶體中,這種記憶體是以ByteArrayOutputStream
                                //來管理的,以下宣告建立一個ByteArrayOutputStream物件備用

                                //Bitmap物件有一個函數compress,可以對圖片進行壓縮並儲存到ByteArrayOutputStream物件中

//                                   bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);//借隱藏1下
                                   testPic.compress(Bitmap.CompressFormat.JPEG, 100, stream);//威威威

                                //ByteArrayOutputStream物件有一個toByteArray函數可以轉出byte[]
                                blob = stream.toByteArray();
                                final long total = blob.length;
            /*
            這張圖是從"拍照的流程"取得
            theImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            //ByteArrayOutputStream物件有一個toByteArray函數可以轉出byte[]
            blob = stream.toByteArray();
            */

                                StorageReference leaf = mStorageRef.child("pic" + user.getUid());
//                                StorageReference leaf = mStorageRef.child("coffee");
                                Log.d("main", "圖片是有沒有傳上去辣" + mStorageRef.child("coffee"));
                                Log.d("main", "圖片是有沒有傳上去辣");
                                UploadTask task1 = leaf.putBytes(blob);
                                task1.addOnSuccessListener(
                                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Log.d("main", "上傳成功");
//                                                Toast.makeText(RegisterActivity.this, "upload....成功", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                );
                                task1.addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("main", "上傳失敗");
//                                                Toast.makeText(RegisterActivity.this, "upload....失敗", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                );
                                task1.addOnCompleteListener(

                                        new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                RegisterActivity.this.runOnUiThread(
                                                        new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                busy.setVisibility(View.INVISIBLE);
                                                            }
                                                        }
                                                );
                                            }
                                        }

                                );
                                //計時器設定
                                task1.addOnProgressListener(
                                        new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                long byte_to_go = taskSnapshot.getBytesTransferred();
                                                //Log.i("上傳", "送出 " + byte_to_go + " BYTEs");

                                                final double every_time_percent = (double) byte_to_go / total;
                                                Log.i("上傳", "送出 " + every_time_percent * 100 + " %");
                                                RegisterActivity.this.runOnUiThread(
                                                        new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                progress.setProgress((int) (every_time_percent * 100));
                                                            }
                                                        }
                                                );

                                            }
                                        }
                                );


                                Intent go = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(go);
                            } else {
                                Toast.makeText(context, "Register fail", Toast.LENGTH_LONG).show();
                                Log.d("main", "創立失敗");
//                                textViewResult.setText("Register fail");
                            }
                        }
                    });
                }
            }
        });
        //設密碼switch
        switchPass = (Switch) findViewById(R.id.switch_pass);
        switchPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchPass.setText("On");
                    editTextPasswordReg.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    switchPass.setText("Off");
                    editTextPasswordReg.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
            }
        });

        imageViewPicReg.setImageResource(R.drawable.userpic);


    }

    @Override//這邊是問你有沒有同意授權使用照相的功能
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    //拍完照的相片會存在哪?存在一個很暫時的記憶體內
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {//INTENT要執行照相功能的識別碼,後面是有沒有按OK
            theImage = (Bitmap) data.getExtras().get("data");//拍完照會存一個預設叫data的檔案
            imageViewPicReg.setImageBitmap(theImage);
            Log.d("照片測試","1"+theImage.toString());
            testPic = theImage;//威威威
        }else if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data !=null){


            Uri selectedImageUri = data.getData();
//            imagepath = getPath(selectedImageUri);
//
//            //依相片的路徑，轉成Bitmap的型態，在ImageView，顯示出選取的相片。
//            theImage= BitmapFactory.decodeFile(imagepath);
//            Log.d("照片測試","2"+theImage.toString());
            try {
                theImage = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

//            imageViewPicReg.setImageURI(selectedImageUri);
            imageViewPicReg.setImageBitmap(theImage);
            testPic = theImage;//威威威
        }
    }
}