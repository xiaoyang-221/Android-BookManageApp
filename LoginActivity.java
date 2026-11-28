package com.example.bookmanageapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookmanageapp.db.MyDBHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText et_username,et_pwd;
    private Button btn_login,btn_register;
    private CheckBox cb_remember;
    private MyDBHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        //注册
        btnRegister();
        //记住密码
        CbRemember();
        //登录
        btnLogin();
    }

    private void CbRemember() {
        boolean isRemember=cb_remember.isChecked();
        cb_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.d("Login","用户选择记住密码");
                } else {
                    Log.d("Login","用户取消记住密码");
                }
            }
        });
    }

    private void btnRegister() {
         btn_register.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
             }
         });
    }

    private void btnLogin() {
       btn_login.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               //获取输入的用户名和密码
               String inputusername=et_username.getText().toString();
               String inputpwd=et_pwd.getText().toString();
               //判断用户名和密码
               if(inputusername.equals("")||inputpwd.equals("")){
                   Toast.makeText(LoginActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
               }else {
                   Cursor cursor=db.rawQuery("select * from tb_userinfo where username=? and pwd=?",new String[]{inputusername,inputpwd});
                   if(cursor.moveToNext()){
                       @SuppressLint("Range") String getusername=cursor.getString(cursor.getColumnIndex("username"));
                       @SuppressLint("Range") String getpwd=cursor.getString(cursor.getColumnIndex("pwd"));
                       if(inputusername.equalsIgnoreCase(getusername) && inputpwd.equalsIgnoreCase(getpwd)){
                           SharedPreferences.Editor editor=getSharedPreferences("userinfo",0).edit();
                           editor.putString("userusername",inputusername);
                           editor.putString("userpwd",inputpwd);
                           editor.commit();
                           Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                           startActivity(intent);
                           finish();
                       }
                   }
               }
           }
       });
    }

    private void initView() {
        et_username=findViewById(R.id.et_username);
        et_pwd=findViewById(R.id.et_password);
        btn_login=findViewById(R.id.btn_login);
        btn_register=findViewById(R.id.btn_register);
        cb_remember=findViewById(R.id.cb_remember);
        helper=new MyDBHelper(LoginActivity.this);
        db=helper.getWritableDatabase();
    }
}