package com.example.commodity_management;

import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    Button register_button;
    ImageButton bt_back;
    EditText register_account;
    EditText register_password;
    EditText register_re_password;
    GetDatabase database;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    ContentValues contentValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        setView();
    }

    private void initView() {
        register_button = findViewById(R.id.register_button);
        bt_back = findViewById(R.id.bt_back);
        register_account = findViewById(R.id.register_account);
        register_password = findViewById(R.id.register_password);
        register_re_password = findViewById(R.id.register_re_password);
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        contentValues = new ContentValues();
    }

    private void setView() {

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private String getAccount(){
        return register_account.getText().toString();
    }

    private String getPassword(){
        return register_password.getText().toString();
    }

    private String getRePassword(){
        return register_re_password.getText().toString();
    }
    private void registerUser() {
        String account = getAccount();
        String password = getPassword();
        String rePassword = getRePassword();

        if (!account.equals("") && !password.equals("") && !rePassword.equals("")){
            if (password.equals(rePassword)){
                cursor = sqLiteDatabase.query("user_table",null,"u_account = ?",new String[]{account},null,null,null);
                if (cursor.moveToFirst()){
                    Toast.makeText(Register.this,"账号已存在！", Toast.LENGTH_SHORT).show();
                }else{
                    contentValues.put("u_account",account);
                    contentValues.put("u_password",password);
                    sqLiteDatabase.insert("user_table",null,contentValues);
                    finish();
                }
            }else{
                Toast.makeText(Register.this,"密码不一致！", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(Register.this,"参数不能为空！", Toast.LENGTH_SHORT).show();
        }
    }
}