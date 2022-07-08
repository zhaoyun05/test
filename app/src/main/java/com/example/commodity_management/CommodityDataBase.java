package com.example.commodity_management;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CommodityDataBase extends SQLiteOpenHelper {
    private static  final String CREATE_TABLE_COMMODITY_TABLE = "create table commodity_table (id integer primary key autoincrement, name text, model text, factory text, address text, " +
            "original_price text, discount_price text, introduction text, uri text ,Details text)";
    private static  final String CREATE_TABLE_USER_TABLE = "create table user_table (id integer primary key autoincrement, u_account text, u_password text)";
    private Context mContext;
    public  CommodityDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_COMMODITY_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists commodity_table");
        sqLiteDatabase.execSQL("drop table if exists user_table");
        onCreate(sqLiteDatabase);
    }
}
