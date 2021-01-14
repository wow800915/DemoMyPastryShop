package com.example.mypastryshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin,buttonRegister;
    private EditText editTextEmail,editTextPassword;
    private String email,password;
    private FirebaseAuth authControl;
    private FirebaseUser currentUser;
    private Context context;
    private TextView textView;
    private SignInButton btSignIn;
    private GoogleSignInClient googleSignInClient;
    private DatabaseReference classDB;
    private FirebaseDatabase fbControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        buttonLogin = (Button) findViewById(R.id.button_login);
        buttonRegister = (Button) findViewById(R.id.button_register);
        textView =findViewById(R.id.textView);

        editTextEmail = (EditText) findViewById(R.id.editText_Email);
        editTextPassword = (EditText) findViewById(R.id.editText_Password);

        context = this;

        authControl = FirebaseAuth.getInstance();
        Log.d("main","authControl = "+authControl);
        fbControl = FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser =authControl.getCurrentUser();
        Log.d("main","firebaseUser = "+firebaseUser);
        classDB = fbControl.getReference("member");
        
        if(firebaseUser != null){
            //WHEN USER ALready sign in，REDIRECT TO　PROFILE ACTIVITY
//            startActivity(new Intent(LoginActivity.this,IndexActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful()){
//                        authControl.signOut();
//                    }
//                }
//            });
        }

        btSignIn= findViewById(R.id.sign_in_button);

        //初始化登入選項
        GoogleSignInOptions googleSignInOptions =new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("647107141614-aaaf4c2asnc5u91g8nqfl8rodohlb5ho.apps.googleusercontent.com")
                .requestEmail().build();

        //Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this,googleSignInOptions);

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //初始化登入企圖
                Intent intent =googleSignInClient.getSignInIntent();
                //Start activity for result
                startActivityForResult(intent,100);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextEmail.length()==0 || editTextPassword.length()==0){
                    Toast.makeText(context,"please input your email & password",Toast.LENGTH_SHORT).show();
                }else {
                    email =editTextEmail.getText().toString();
                    password  = editTextPassword.getText().toString();

                    currentUser = authControl.getCurrentUser();
                    if(currentUser != null){
                        authControl.signOut();
                    }

                    authControl.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener((Activity)context,new OnCompleteListener<AuthResult>(){
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Log.d("main","Login ok");
                                        SharedPreferences sp = getSharedPreferences("user", 0);
                                        sp.edit().putString("name", editTextEmail.getText().toString()).commit();
                                        sp.edit().putString("account",authControl.getUid()).commit();
                                        Intent go = new Intent(LoginActivity.this, IndexActivity.class);
                                        LoginActivity.this.startActivity(go);
                                    }else{
                                        Log.d("main","Login fail");
                                        Toast.makeText(context,"wrong Email or wrong password",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(go);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            //when request code is equal to 100
            //Initialize task
            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            //Check condition
            if(signInAccountTask.isSuccessful()){
                //when google sign in 成功
                //Initialize string
                String s ="Google sign in successful";
                displayToast(s);
                try {
                    //Initialize sign in account
                    GoogleSignInAccount googleSignInAccount =signInAccountTask
                            .getResult(ApiException.class);
                    if(googleSignInAccount != null){
                        //When sign in account is not equal to null
                        //Initalize auth credential
                        AuthCredential authCredential= GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken(),null);
                    //check credential
                    authControl.signInWithCredential(authCredential)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //Check condition
                                    if(task.isSuccessful()){
                                        //When task is successful
                                        //Redirect to profile activity

                                        FirebaseUser user = authControl.getCurrentUser();
                                        classDB.child(user.getUid()).child("Email").setValue(user.getEmail());
                                        classDB.child(user.getUid()).child("message").setValue("如有任何問題歡迎留言,服務專員會盡快為您服務");
//                                        classDB.child(user.getUid()).child("Address").setValue("尚未輸入地址");
                                        SharedPreferences sp = getSharedPreferences("user", 0);
                                        sp.edit().putString("name", user.getEmail()).commit();
                                        sp.edit().putString("account",authControl.getUid()).commit();

                                        displayToast("Firebase authentication successful");

                                        startActivity(new Intent(LoginActivity.this,IndexActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    }else {
                                        //when task is unsuccessful Display toast
                                        displayToast("Authentication Failed :"+task.getException().getMessage());
                                    }
                                }
                            });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void displayToast(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}