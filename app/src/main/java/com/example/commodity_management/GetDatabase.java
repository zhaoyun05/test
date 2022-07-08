package com.example.commodity_management;

import android.content.Context;

public class GetDatabase {
    public CommodityDataBase getDatabase(Context context){
        return new CommodityDataBase(context,"commodity_database",null,2);
    }
}
