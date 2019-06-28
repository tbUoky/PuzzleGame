package com.example.lenovo.puzzlegame.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
public class DBOpenHelper extends SQLiteOpenHelper {
    public  DBOpenHelper(Context context, String name, CursorFactory factory,
                        int version){
        super(context, name, factory, version);
    }
    @Override
    //首次创建数据库的时候调用，一般可以执行建库，建表的操作
    //Sqlite没有单独的布尔存储类型，它使用INTEGER作为存储类型，0为false，1为true
    public void onCreate(SQLiteDatabase db){
        //user table
        db.execSQL("create table if not exists user_tb(_id integer primary key autoincrement," +
                "Username VARCHAR(40) not null," +
                //"CompleteTime text ,"+
                "Password VARCHAR(30) not null)");
        db.execSQL("create table if not exists game_tb(_id integer primary key autoincrement," +
                "User_Type VARCHAR(2) not null," +
                "Username VARCHAR(40) not null," +
                "Game_Level integer not null," +
                "Game_Time integer not null ,"+
                //Over_Time
                "Over_Time VARCHAR(20) not null," +
                "Game_Step integer not null)");
    }

    @Override//当数据库的版本发生变化时，会自动执行
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("drop table if exists user_tb");
        db.execSQL("drop table if exists  game_tb");
        onCreate(db);
    }
}
