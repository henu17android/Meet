package com.example.meet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meet.activity.MainActivity;
import com.example.meet.fragment.SettingFragment;


public class LoginActivity extends AppCompatActivity {
    private TextView tv_main_title;//标题
    private table dbHelper;
    private TextView tv_back,tv_register;//返回键,显示的注册，找回密码


    private Button btn_login;//登录按钮
    private String userName,psw;//获取的用户名，密码，加密密码
    private EditText et_user_name,et_psw;//编辑框
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new table(this,"UserStore.db",null,1);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }
    //获取界面控件
    private void init() {
        //从main_title_bar中获取的id
        tv_main_title=findViewById(R.id.tv_main_title);
        tv_main_title.setText("登录");
        tv_back=findViewById(R.id.tv_back);
        //从activity_login.xml中获取的
        tv_register=findViewById(R.id.tv_register);

        btn_login=findViewById(R.id.btn_login);
        et_user_name=findViewById(R.id.et_user_name);
        et_psw=findViewById(R.id.et_psw);
        //返回键的点击事件
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录界面销毁
                LoginActivity.this.finish();
            }
        });
        //立即注册控件的点击事件
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //为了跳转到注册界面，并实现注册功能
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        //找回密码控件的点击事件

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始登录，获取用户名和密码 getText().toString().trim();
                userName=et_user_name.getText().toString().trim();
                psw=et_psw.getText().toString().trim();

                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;

                }

               else if (login(userName,psw)) {
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    return;
                }
                else {
                    Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }

    private String readPsw(String userName){
        //getSharedPreferences("loginInfo",MODE_PRIVATE);
        //"loginInfo",mode_private; MODE_PRIVATE表示可以继续写入
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //sp.getString() userName, "";
        return sp.getString(userName , "");
    }
    public boolean login(String username,String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from userData where name=? and password=?";
        Cursor cursor = db.rawQuery(sql, new String[] {username, password});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }else{
        return false;}
    }

    /**
     *保存登录状态和登录用户名到SharedPreferences中
     */

    /**

     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 数据
     */

    //显示数据， onActivityResult
    //startActivityForResult(intent, 1); 从注册界面中获取数据
    //int requestCode , int resultCode , Intent data
    // LoginActivity -> startActivityForResult -> onActivityResult();

}


