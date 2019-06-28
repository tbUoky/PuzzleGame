package com.example.lenovo.puzzlegame.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.puzzlegame.Utils.MyApp;
import com.example.lenovo.puzzlegame.R;
import com.example.lenovo.puzzlegame.Utils.DBOpenHelper;

public class SignupActivity extends AppCompatActivity {
    private MyApp myApp;
    private Button btn_sign_regist = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        myApp = (MyApp) getApplication();
        btn_sign_regist =  findViewById(R.id.btn_sign_regist);
        //设置Button监听
        btn_sign_regist.setOnClickListener(new MyButtonListener());


    }
    // 实现OnClickListener接口
    private class MyButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            //对用户输入的值的格式进行判断的处理...
            String username="";
            String pwd="";
            EditText sign_username_et =(EditText)findViewById(R.id.sign_username_et);
            EditText sign_pwd_et =(EditText)findViewById(R.id.sign_pwd_et);
            EditText sign_pwd_again_et = findViewById(R.id.sign_pwd_again_et);
            username = sign_username_et.getText().toString();
            pwd = sign_pwd_et.getText().toString();
            String again_pwd=sign_pwd_again_et.getText().toString();
             if (username.length()==0||pwd.length()==0||again_pwd.length()==0){
                 Toast.makeText(SignupActivity.this, "不能为空！", Toast.LENGTH_SHORT).show();
                 return;
             }
            if (!pwd.equals(again_pwd)){
                Toast.makeText(SignupActivity.this,"两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                sign_pwd_et.setText("");
                sign_pwd_again_et.setText("");
                return;
            }
            //调用DBOpenHelper
            DBOpenHelper helper = new DBOpenHelper(SignupActivity.this,"puzzle.db",null,1);
            SQLiteDatabase db = helper.getWritableDatabase();
            //根据画面上输入的账号去数据库中进行查询
            Cursor c = db.query("user_tb",null,"Username=? and length(Password)<=15",new String[]{username},null,null,null);
            //如果有查询到数据，则说明账号已存在
            if(c!=null && c.getCount() >= 1){
                Toast.makeText(SignupActivity.this, username+"用户已存在", Toast.LENGTH_SHORT).show();
                c.close();
            }
            //如果没有查询到数据，则往数据库中insert一笔数据
            else{
                Toast.makeText(SignupActivity.this, "insert data", Toast.LENGTH_SHORT).show();

                //insert data
                ContentValues values= new ContentValues();
                values.put("Username",username);
                values.put("Password",pwd);
                long rowid = db.insert("user_tb",null,values);
                Toast.makeText(SignupActivity.this, username+"注册成功", Toast.LENGTH_SHORT).show();//提示信息
                Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                myApp.setUsername(username);
                myApp.setUserType(1);
                startActivity(intent);
                SignupActivity.this.finish();
            }
            db.close();
        }
    }

    public void GoLoginPage(View view){
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
