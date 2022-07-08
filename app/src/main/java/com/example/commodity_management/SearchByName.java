package com.example.commodity_management;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchByName extends AppCompatActivity {

    Button search_button_search;
    EditText search_text_search;
    SQLiteDatabase sqLiteDatabase;
    GetDatabase database;
    Cursor cursor;
    ListView search_commodity_list;
    CommodityAdapter commodityAdapter;
    ImageButton bt_back;
    List<Commodity> adapterList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_name);
        initView();
        setView();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 4:
                System.out.println("查看是否执行本回调");
                setAdapter();
                break;
        }
    }

    private void initView() {
        search_button_search = findViewById(R.id.search_button_search);
        search_text_search = findViewById(R.id.search_text_search);
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        search_commodity_list = findViewById(R.id.search_commodity_list);
        bt_back = findViewById(R.id.bt_back);

    }

    private void setView() {

        search_text_search.setEnabled(true);
        search_text_search.setFocusable(true);
        search_text_search.setFocusableInTouchMode(true);
        search_text_search.requestFocus();
        search_button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getInputName().equals("")){
                    setAdapter();
                }else {
                    Toast.makeText(SearchByName.this,"输入为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        search_commodity_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Commodity commodity =adapterList.get(position);
                int commodityId = queryID(commodity.getName(),commodity.getModel(),commodity.getFactory(),commodity.getIntroduction());
                System.out.println("item的id："+commodityId);
                Intent intent = new Intent(SearchByName.this,Show_Commodity.class);
                intent.putExtra("id",commodityId);
                startActivityForResult(intent,4);
            }
        });

        search_commodity_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchByName.this);
                builder.setTitle("提示");
                builder.setMessage("确认删除商品");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCommodityItem(position);
                        Toast.makeText(SearchByName.this,"删除成功!",Toast.LENGTH_SHORT).show();
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

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }



    private List<Commodity> searchByName(String searchName){
        List<Commodity> list = new ArrayList<>();
        Commodity commodity = null;
        cursor = sqLiteDatabase.query("commodity_table",null,"name like ?", new String[]{"%"+searchName+"%"},null,null,null);
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String uri = cursor.getString(cursor.getColumnIndex("uri"));
                String model = cursor.getString(cursor.getColumnIndex("model"));
                String factory = cursor.getString(cursor.getColumnIndex("factory"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String original_price = cursor.getString(cursor.getColumnIndex("original_price"));
                String discount_price = cursor.getString(cursor.getColumnIndex("discount_price"));
                String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
                String Details = cursor.getString(cursor.getColumnIndex("Details"));
                commodity = new Commodity(name,model,factory,address,original_price,discount_price,introduction,uri,Details);
                list.add(commodity);
            }while (cursor.moveToNext());
        }
        return list;
    }

    private void deleteCommodityItem(int i){
        Commodity commodity = adapterList.get(i);
        adapterList.remove(i);
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

    private void setAdapter(){
        adapterList = searchByName(getInputName());
        commodityAdapter = new CommodityAdapter(SearchByName.this,adapterList);
        search_commodity_list.setAdapter(commodityAdapter);
        commodityAdapter.notifyDataSetChanged();
    }

    private String getInputName(){
        return search_text_search.getText().toString();
    }
}