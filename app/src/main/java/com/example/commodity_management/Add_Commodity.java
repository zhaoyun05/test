package com.example.commodity_management;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class Add_Commodity extends AppCompatActivity {
    ImageView imageView;
    Button choose_button;
    EditText add_title_text;
    EditText add_model_text;
    EditText add_factory_text;
    EditText add_address_text;
    EditText add_original_price_text;
    EditText add_discount_price_text;
    EditText add_introduction_text;
    EditText add_Details_text;
    Button add_commodity_button;
    GetDatabase database;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    ImageButton bt_back;
    Uri uri;
    String str_uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__commodity);
        initView();
        setView();
    }

    @Override
    public void onBackPressed() {

        showAlertDialog();
    }

    private void initView() {
        imageView = findViewById(R.id.add_image);
        choose_button = findViewById(R.id.add_choose);
        add_title_text = findViewById(R.id.add_title_text);
        add_model_text = findViewById(R.id.add_model_text);
        add_factory_text = findViewById(R.id.add_factory_text);
        add_address_text = findViewById(R.id.add_address_text);
        add_original_price_text = findViewById(R.id.add_original_price_text);
        add_discount_price_text = findViewById(R.id.add_discount_price_text);
        add_introduction_text = findViewById(R.id.add_introduction_text);
        add_Details_text = findViewById(R.id.add_Details_text);
        add_commodity_button = findViewById(R.id.add_commodity_button);
        contentValues = new ContentValues();
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        bt_back = findViewById(R.id.bt_back);
    }

    private void setView() {
//    监听用户点击添加商品按钮
        add_commodity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                调用数据库插入方法
                insertCommodity();
            }
        });
//监听用户点击返回按钮
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                调用弹出提示框方法
                 showAlertDialog();
            }
        });
        choose_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 3);

            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                uri = data.getData();
                str_uri =  getRealPathFromURI(uri);
                imageView.setImageURI(uri);
            }
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

//    提示框方法
    private void showAlertDialog(){
        String name = getTitleText();
        String model = getModelText();
        String factory = getFactoryText();
        String address = getAddressText();
        String original_price = getOriginalPrice();
        String discount_price = getDiscountPrice();
        String introduction = getIntroduction();
        String Details = getDetails();
        if (!name.equals("") && !model.equals("") && !factory.equals("") && !address.equals("") && !original_price.equals("") && !discount_price.equals("") && !introduction.equals("") && !Details.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(Add_Commodity.this);
//           设置提示框标题
            builder.setTitle("提示");
//            设置提示框提示文字
            builder.setMessage("离开将会清除所有填写数据，是否保存！");
//            监听提示框点击保存按钮
            builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    保存数据到数据库方法
                    insertCommodity();
                }
            });
//            监听用户点击提示框取消按钮
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
//            让提示框显示出来
            builder.show();
        }else{
//            关闭页面
            finish();
        }
    }

    private void insertCommodity(){
        String name = getTitleText();
        String model = getModelText();
        String factory = getFactoryText();
        String address = getAddressText();
        String str_uri = getUriText();
        String original_price = getOriginalPrice();
        String discount_price = getDiscountPrice();
        String introduction = getIntroduction();
        String Details = getDetails();
        if (!name.equals("") && !model.equals("") && !factory.equals("") && !address.equals("")&& !str_uri.equals("") && !original_price.equals("") && !discount_price.equals("") && !introduction.equals("") && !Details.equals("")){
            System.out.println(name);
//            设置数据库对应字段的值
            contentValues.put("name",name);
            contentValues.put("model",model);
            contentValues.put("factory",factory);
            contentValues.put("address",address);
            contentValues.put("original_price",original_price);
            contentValues.put("discount_price",discount_price);
            contentValues.put("introduction",introduction);
            contentValues.put("uri",str_uri);
            contentValues.put("Details",Details);
//            将数据插入到数据库中
            sqLiteDatabase.insert("commodity_table",null,contentValues);
            contentValues.clear();
            Toast.makeText(Add_Commodity.this,"添加成功",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Add_Commodity.this,MainActivity.class));
            finish();
        }else {
            Toast.makeText(Add_Commodity.this,"请填写必填参数！", Toast.LENGTH_SHORT).show();
        }
    }

    private String getTitleText(){
        return add_title_text.getText().toString();
    }
    private String getUriText(){
        return str_uri ;
    }


    private String getModelText(){
        return add_model_text.getText().toString();
    }

    private String getFactoryText(){
        return add_factory_text.getText().toString();
    }

    private String getAddressText(){
        return add_address_text.getText().toString();
    }

    private String getOriginalPrice(){
        return add_original_price_text.getText().toString();
    }

    private String getDiscountPrice(){
        return add_discount_price_text.getText().toString();
    }

    private String getIntroduction(){
        return add_introduction_text.getText().toString();
    }

    private String getDetails(){
        return add_Details_text.getText().toString();
    }
}