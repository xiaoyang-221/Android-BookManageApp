package com.example.bookmanageapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DBNAME="book.db";
    private static final int VERSION=1;

    public MyDBHelper(@NonNull Context context){
        super(context,DBNAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tb_userinfo(id integer primary key autoincrement,name varchar(10),username varchar(20),pwd varchar(15),email varchar(50))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
