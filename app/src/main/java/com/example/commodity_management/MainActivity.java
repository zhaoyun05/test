package com.example.commodity_management;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends CheckPermissionsActivity  {
    ListView main_commodity_list;
    List<Commodity> list;
    GetDatabase database;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    CommodityAdapter commodityAdapter;
    ImageButton edit_button;
    SharedPreferences.Editor dataEdit;
    SharedPreferences getDataEdit;
    Button main_add_commodity;
    Button main_go_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setView();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       switch (requestCode){
           case 2:
           case 3:
              initView();
               break;
       }
    }

    private void initView() {
        main_commodity_list = findViewById(R.id.main_commodity_list);
        list = new ArrayList<Commodity>();
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        edit_button = findViewById(R.id.edit_button);
        dataEdit = getSharedPreferences("user_data",MODE_PRIVATE).edit();
        getDataEdit = getSharedPreferences("user_data",MODE_PRIVATE);
        main_add_commodity = findViewById(R.id.main_add_commodity);
        main_go_search = findViewById(R.id.main_go_search);
        onLoadCommodityList();
    }

    private void setView() {
        main_commodity_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Commodity commodity =list.get(position);
                int commodityId = queryID(commodity.getName(),commodity.getModel(),commodity.getFactory(),commodity.getIntroduction());
                System.out.println("item的id："+commodityId);
                Intent intent = new Intent(MainActivity.this,Show_Commodity.class);
                intent.putExtra("id",commodityId);
                startActivityForResult(intent,2);
            }
        });

        main_commodity_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确认删除商品");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCommodityItem(position);
                        Toast.makeText(MainActivity.this,"删除成功!",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return true;
            }
        });

        main_add_commodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Add_Commodity.class);
                startActivityForResult(intent,3);
            }
        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,edit_button);
                popupMenu.getMenuInflater().inflate(R.menu.menu_pop,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.clear_auto_login:
                                dataEdit.putBoolean("isAutoLogin",false);
                                dataEdit.commit();
                                break;
                            case R.id.exit_login:
                                dataEdit.putBoolean("isAutoLogin",false);
                                dataEdit.commit();
                                startActivity(new Intent(MainActivity.this,Login.class));
                                finish();
                                break;
                            default:
                                break;
                        }

                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        main_go_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchByName.class);
                startActivityForResult(intent,3);
            }
        });
    }

    private void onLoadCommodityList(){
        cursor = sqLiteDatabase.query("commodity_table",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String uri = cursor.getString(cursor.getColumnIndex("uri"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                Log.e("HAHA",uri);
                String model = cursor.getString(cursor.getColumnIndex("model"));
                String factory = cursor.getString(cursor.getColumnIndex("factory"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String original_price = cursor.getString(cursor.getColumnIndex("original_price"));
                String discount_price = cursor.getString(cursor.getColumnIndex("discount_price"));
                String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
                String Details = cursor.getString(cursor.getColumnIndex("Details"));
                Commodity commodity = new Commodity(name,model,factory,address,original_price,discount_price,introduction,uri,Details);
                list.add(commodity);
            }while (cursor.moveToNext());
        }
        commodityAdapter = new CommodityAdapter(this,list);
        main_commodity_list.setAdapter(commodityAdapter);
        commodityAdapter.notifyDataSetChanged();
    }

    private void deleteCommodityItem(int i){
        Commodity commodity = list.get(i);
        list.remove(i);
        sqLiteDatabase.delete("commodity_table","name = ? and model = ? and factory = ? and introduction = ?", new String[]{
                commodity.getName(), commodity.getModel(),  commodity.getFactory(),  commodity.getIntroduction()
        });
        commodityAdapter.notifyDataSetChanged();
    }

    private int queryID(String name, String model, String factory, String introduction) {
        int id = -1;
        cursor = sqLiteDatabase.query("commodity_table",null,"name = ? and model = ? and factory = ? and introduction = ?", new String[]{
                name, model, factory, introduction
        },null, null,null);

        if (cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"));
            }while (cursor.moveToNext());
        }
        return id;
    }
}