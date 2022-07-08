package com.example.commodity_management;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Show_Commodity extends AppCompatActivity {
    ImageView dis_img;
    TextView show_title;
    TextView show_introduction;
    TextView show_model;
    TextView show_original_price;
    TextView show_discount_price;
    TextView show_Details;
    TextView show_factory;
    TextView show_address;
    ImageButton bt_back;
    Button my_title_edit;
    Intent globalIntent;
    GetDatabase database;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    Commodity commodity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__commodity);
        initView();
        setView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Commodity commodity = null;
        System.out.println(requestCode);
        switch (requestCode){
            case 1:
                    if (resultCode == RESULT_OK){
                        commodity = queryCommodity(data.getStringExtra("id"));
                        this.commodity = commodity;
                        showData();
                        System.out.println(commodity);
                    }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    private void initView() {
        dis_img = findViewById(R.id.dis_img);
        show_title = findViewById(R.id.show_title);
        show_introduction = findViewById(R.id.show_introduction);
        show_model = findViewById(R.id.show_model);
        show_original_price = findViewById(R.id.show_original_price);
        show_discount_price = findViewById(R.id.show_discount_price);
        show_Details = findViewById(R.id.show_Details);
        show_factory = findViewById(R.id.show_factory);
        show_address = findViewById(R.id.show_address);
        bt_back = findViewById(R.id.bt_back);
        my_title_edit = findViewById(R.id.my_title_edit);
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        globalIntent = getIntent();
        commodity = queryCommodity(String.valueOf(globalIntent.getIntExtra("id",-1)));
    }

    private void setView() {
        showData();
        show_discount_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        my_title_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Show_Commodity.this,EditCommodity.class);
                intent.putExtra("id",globalIntent.getIntExtra("id",-1));
                startActivityForResult(intent,1);
            }
        });
    }

    private void showData() {
        show_title.setText(getTitleText());
        show_introduction.setText("商品简介："+getIntroduction());
        show_model.setText("商品型号："+getModelText());
        show_original_price.setText("￥"+getDiscountPrice());
        show_discount_price.setText("￥"+getOriginalPrice());
        show_Details.setText("商品详情："+getDetails());
        dis_img.setImageURI(Uri.parse(getUriText()));



        show_factory.setText(getFactoryText());
        show_address.setText(getAddressText());
    }

    private Commodity queryCommodity(String id){
        Commodity commodity = null;
        cursor = sqLiteDatabase.query("commodity_table",null,"id = ?",new String[]{id},null,null,null);
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
            }while (cursor.moveToNext());
        }
        System.out.println(commodity);
        return commodity;
    }

    private String getTitleText(){
        return commodity.getName();
    }
    private String getUriText(){
        return commodity.getUri();
    }

    private String getModelText(){
        return commodity.getModel();
    }

    private String getFactoryText(){
        return commodity.getFactory();
    }

    private String getAddressText(){
        return commodity.getAddress();
    }

    private String getOriginalPrice(){
        return commodity.getOriginal_price();
    }

    private String getDiscountPrice(){
        return commodity.getDiscount_price();
    }

    private String getIntroduction(){
        return commodity.getIntroduction();
    }

    private String getDetails(){
        return commodity.getDetails();
    }
}