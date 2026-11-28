package com.example.bookmanageapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookmanageapp.db.MyDBHelper;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_name,et_username,et_pwd,et_email;
    private Button btn_register;
    private MyDBHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //初始化
        et_name=findViewById(R.id.et_name);
        et_username=findViewById(R.id.et_username);
        et_pwd=findViewById(R.id.et_password);
        et_email=findViewById(R.id.et_email);
        btn_register=findViewById(R.id.btn_register);
        helper=new MyDBHelper(RegisterActivity.this);
        db=helper.getWritableDatabase();
        //注册
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values=new ContentValues();
                values.put("name",et_name.getText().toString());
                values.put("username",et_username.getText().toString());
                values.put("pwd",et_pwd.getText().toString());
                values.put("email",et_email.getText().toString());
                db.insert("tb_userinfo",null,values);
                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
            }
        });
    }
}