package com.example.meet.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meet.R;

public class RegisterActivity extends AppCompatActivity {
    private table dbHelper;
    private TextView tv_main_title;//标题
    private TextView tv_back;//返回按钮
    private Button btn_register;//注册按钮
    //用户名，密码，再次输入的密码的控件
    private EditText et_user_name,et_psw,et_psw_again;
    //用户名，密码，再次输入的密码的控件的获取值
    private String userName,psw,pswAgain;
    //标题布局
    private RelativeLayout rl_title_bar;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置页面布局 ,注册界面
        setContentView(R.layout.activity_register);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        dbHelper = new table(this,"UserStore.db",null,1);
    }


    private void init() {
        //从main_title_bar.xml 页面布局中获取对应的UI控件
        tv_main_title=findViewById(R.id.tv_main_title);
        tv_main_title.setText("注册");
        tv_back=findViewById(R.id.tv_back);
        //布局根元素
        rl_title_bar=findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.TRANSPARENT);
        //从activity_register.xml 页面中获取对应的UI控件
        btn_register=findViewById(R.id.btn_register);
        et_user_name=findViewById(R.id.et_user_name);
        et_psw=findViewById(R.id.et_psw);
        et_psw_again=findViewById(R.id.et_psw_again);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回键
                RegisterActivity.this.finish();
            }
        });
        //注册按钮
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入在相应控件中的字符串
                getEditString();
                //判断输入框内容
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(pswAgain)) {
                    Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!psw.equals(pswAgain)) {
                    Toast.makeText(RegisterActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
                    return;}
                    else if (psw.length()<6) {
                        Toast.makeText(RegisterActivity.this, "密码不少六位", Toast.LENGTH_SHORT).show();
                        return;

                } else if (CheckIsDataAlreadyInDBorNot(userName)) {
                    Toast.makeText(RegisterActivity.this, "该用户已被注册", Toast.LENGTH_SHORT).show();
                } else
                    if (register(userName, psw)) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        Intent data = new Intent();
                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                        // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    }
                    }





            //注册成功后把账号传递到LoginActivity.java中
            // 返回值到loginActivity显示


        });
    }
    public boolean register(String username,String password)
    { SQLiteDatabase db= dbHelper.getWritableDatabase();

     ContentValues values=new ContentValues();
     values.put("name",username);
     values.put("password",password);
     db.insert("userData",null,values);
     db.close();
     //db.execSQL("insert into userData (name,password) values (?,?)",new String[]{username,password});
        return true;}
    public boolean CheckIsDataAlreadyInDBorNot(String value)
        { SQLiteDatabase db=dbHelper.getWritableDatabase();
        String Query = "Select * from userData where name =?";
        Cursor cursor = db.rawQuery(Query,new String[] { value });
        if (cursor.getCount()>0){ cursor.close(); return true; }
        else {
        cursor.close(); return false; }}



    private void getEditString(){
        userName=et_user_name.getText().toString().trim();
        psw=et_psw.getText().toString().trim();
        pswAgain=et_psw_again.getText().toString().trim();
    }

        }

