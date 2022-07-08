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

public class EditCommodity extends AppCompatActivity {
    Uri uri;
    String str_uri;
    Button choose_image_button;
    ImageView get_image_view;
    EditText edit_title;
    EditText edit_model;
    EditText edit_factory;
    EditText edit_address;
    EditText edit_original_price;
    EditText edit_discount_price;
    EditText edit_introduction;
    EditText edit_Details;
    Button edit_commodity_button;
    GetDatabase database;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    Cursor cursor;
    Intent globalIntent;
    ImageButton btn_back;
    String id;
    Commodity commodity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_commodity);
        initView();
        setView();


    }



    private void initView() {
        get_image_view = findViewById(R.id.imageview);
        choose_image_button = findViewById(R.id.choose);
        edit_title = findViewById(R.id.edit_title);
        edit_model = findViewById(R.id.edit_model);
        edit_factory = findViewById(R.id.edit_factory);
        edit_address = findViewById(R.id.edit_address);
        edit_original_price = findViewById(R.id.edit_original_price);
        edit_discount_price = findViewById(R.id.edit_discount_price);
        edit_introduction = findViewById(R.id.edit_introduction);
        edit_Details = findViewById(R.id.edit_Details);
        edit_commodity_button = findViewById(R.id.edit_commodity_button);
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        contentValues = new ContentValues();
        globalIntent = getIntent();
        btn_back = findViewById(R.id.bt_back);
        id = String.valueOf(globalIntent.getIntExtra("id",-1));
        System.out.println("获取到之前页面传递过来的id："+id);
        commodity = queryCommodity(id);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    private void setView() {
        showData();
        edit_commodity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCommodity(id);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();

            }
        });
        choose_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);

            }
        });

    }


    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditCommodity.this);
        builder.setTitle("提示");
        builder.setMessage("是否保存当前修改");
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateCommodity(id);
                Toast.makeText(EditCommodity.this,"保存成功!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra("id",id);
                setResult(RESULT_CANCELED,intent);
                finish();
                finish();
            }
        });
        builder.show();
    }
    private void updateCommodity(String id) {
        System.out.println("传递过来的id：" + id);
        if (Integer.parseInt(id) < 0){
            Toast.makeText(EditCommodity.this,"数据异常！",Toast.LENGTH_SHORT).show();
        }else {
            Commodity commodity = getEditCommodityInfo();
            System.out.println(commodity.getName());
            contentValues.put("name", commodity.getName());
            contentValues.put("model", commodity.getModel());
            contentValues.put("factory", commodity.getFactory());
            contentValues.put("address", commodity.getAddress());
            contentValues.put("original_price", commodity.getOriginal_price());
            contentValues.put("discount_price", commodity.getDiscount_price());
            contentValues.put("introduction", commodity.getIntroduction());
            contentValues.put("Details", commodity.getDetails());
            contentValues.put("uri", getStr_uriText());
            sqLiteDatabase.update("commodity_table",contentValues,"id = ?",new String[]{id});
            contentValues.clear();
            Toast.makeText(EditCommodity.this,"修改成功！",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("id",id);
            System.out.println("设置的 ID:" + id);
            setResult(RESULT_OK,intent);
            finish();
        }
    }


    private void showData() {
        edit_title.setText(getTitleText());
        edit_introduction.setText(getIntroduction());
        edit_model.setText(getModelText());
        edit_discount_price.setText(getDiscountPrice());
        edit_original_price.setText(getOriginalPrice());
        edit_Details.setText(getDetails());
        edit_factory.setText(getFactoryText());
        edit_address.setText(getAddressText());
    }

    private Commodity queryCommodity(String id){
        Commodity commodity = null;
        cursor = sqLiteDatabase.query("commodity_table",null,"id = ?",new String[]{id},null,null,null);
        if (cursor.moveToFirst()){
            do {
                String uri = cursor.getString(cursor.getColumnIndex("uri"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
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
        return commodity;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                uri = data.getData();
                str_uri = getRealPathFromURI(uri);
                get_image_view.setImageURI(uri);
            }
        }
    }
    private Commodity getEditCommodityInfo(){
        return new Commodity(edit_title.getText().toString(), edit_model.getText().toString(), edit_factory.getText().toString(), edit_address.getText().toString(),
                edit_original_price.getText().toString(), edit_discount_price.getText().toString(), edit_introduction.getText().toString(), String.valueOf(uri),edit_Details.getText().toString());
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

    private String getStr_uriText(){
        return str_uri;
    }
    private String getTitleText(){
        return commodity.getName();
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