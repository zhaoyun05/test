package com.example.commodity_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {


//    定义组件
    EditText login_editText_account;
    EditText login_editText_password;
    Button login_button;
    CheckBox login_checkBox_remember;
    CheckBox login_checkBox_auto_login;
    Button login_text_goRegister;
    SQLiteDatabase sqLiteDatabase;
    GetDatabase database;
    Cursor cursor;
    SharedPreferences.Editor dataEdit;
    SharedPreferences getDataEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        初始化数据
        viewInit();
//        设置视图逻辑
        setView();
    }

//    初始化数据
    private void viewInit() {
        login_editText_account = findViewById(R.id.login_editText_account);
        login_editText_password = findViewById(R.id.login_editText_password);
        login_button = findViewById(R.id.login_button);
        login_checkBox_auto_login = findViewById(R.id.login_checkBox_auto_login);
        login_checkBox_remember = findViewById(R.id.login_checkBox_remember);
        login_text_goRegister = findViewById(R.id.login_text_goRegister);
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        dataEdit = getSharedPreferences("user_data",MODE_PRIVATE).edit();
        getDataEdit = getSharedPreferences("user_data",MODE_PRIVATE);
//      判断是否是记住密码，如果是记住密码，那么将本地缓存中的账户密码填入EditText中，并且还原记住密码和自动登录的状态
        if (getDataEdit.getBoolean("isRemember",false)){
            login_editText_account.setText(getDataEdit.getString("u_account",""));
            login_editText_password.setText(getDataEdit.getString("u_password",""));
            login_checkBox_remember.setChecked(getDataEdit.getBoolean("isRemember",false));
            login_checkBox_auto_login.setChecked(getDataEdit.getBoolean("isAutoLogin",false));
        }
//        如果是自动登录，则调用登录方法进行登录
        if (getDataEdit.getBoolean("isAutoLogin",false)){
            login();
        }
    }


//   设置活动的监听逻辑
    private void setView() {

//        如果点击登录按钮，则调用登录方法进行登录
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

//        监听点击记住密码选项
        login_checkBox_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                判断如果没有被选中记住密码，则将自动登录选框设置为false
                if (!isChecked){
                    login_checkBox_auto_login.setChecked(false);
                }
//                将记住密码和自动登录的选框状态进行保存
                dataEdit.putBoolean("isRemember",getRemember());
                dataEdit.putBoolean("isAutoLogin",getAutoLogin());
                dataEdit.commit();
            }
        });

//        监听自动登录选框
        login_checkBox_auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                如果自动登录选框被勾选，则将记住密码选框进行勾选
                if (isChecked){
                    login_checkBox_remember.setChecked(true);
                }
//                将记住密码和自动登录选框状态进行保存
                dataEdit.putBoolean("isRemember",getRemember());
                dataEdit.putBoolean("isAutoLogin",getAutoLogin());
                dataEdit.commit();
            }
        });

//        监听点击注册按钮,如果点击了注册按钮,则调用跳转到注册页面方法
        login_text_goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterActivity();
            }
        });
    }

//    获取用户输入的账户
    private String getAccount(){
        return  login_editText_account.getText().toString();
    }
//    获取用户输入的密码
    private String getPassword(){
        return  login_editText_password.getText().toString();
    }
//  获取记住密码选框的状态
    private boolean getRemember(){
        return login_checkBox_remember.isChecked();
    }
//  获取自动登录选框的状态
    private boolean getAutoLogin(){
        return login_checkBox_auto_login.isChecked();
    }

//    登录方法
    private void login(){
//        获取到用户输入的账号
        String account = getAccount();
//        获取用户输入的密码
        String password = getPassword();
//        判断账户或者是密码是否为空
        if (!account.equals("") && !password.equals("")){
//            如果账户和密码都不为空那么调用数据库查询user_table表，并且查询条件为用户输入的账户
            cursor = sqLiteDatabase.query("user_table",null,"u_account = ?",new String[]{account},null,null,null);
//            判断是否有数据返回，也就是是否查用户输入的用户信息
            if (cursor.moveToFirst()){
                //存在数据，对数据进行遍历
                do {
//                    判断用户输入的账号和密码是否匹配
                    if (account.equals(cursor.getString(cursor.getColumnIndex("u_account"))) && password.equals(cursor.getString(cursor.getColumnIndex("u_password")))){
//                        如果账户和密码匹配则调研本地缓存方法
                        setLocalStorage();
//                        跳转到主页面
                        startActivity(new Intent(Login.this,MainActivity.class));
//                        关闭当前页面
                        finish();
                    }else {
//                        提示密码错误
                        Toast.makeText(Login.this,"密码错误！",Toast.LENGTH_SHORT).show();
                    }
                }while (cursor.moveToNext());
            }else{
//                提示账户不存在
                Toast.makeText(Login.this,"账号不存在！",Toast.LENGTH_SHORT).show();
            }
        }else {
//            如果密码或者是账号为空则提示账号密码不能为空
            Toast.makeText(Login.this,"账号或密码不能为空！",Toast.LENGTH_SHORT).show();
        }
    }

//    跳转到注册页面方法
    private void goToRegisterActivity(){
        startActivity(new Intent(Login.this,Register.class));
    }

//    本地缓存方法
    private void setLocalStorage(){
//        判断是否勾选了记住密码，如果勾选了记住密码则将数据保存到本地
        if (getRemember()){
            dataEdit.putString("u_account",getAccount());
            dataEdit.putString("u_password",getPassword());
        }else{
//            如果没有勾选，则将账户密码设置为空
            dataEdit.putString("u_account","");
            dataEdit.putString("u_password","");
        }
//        提交保存的数据
        dataEdit.commit();
    }
}