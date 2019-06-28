package com.example.lenovo.puzzlegame.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lenovo.puzzlegame.R;

public class SplashActivity extends AppCompatActivity {
    private static int TIME = 3;
    private TextView mTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mTime = (TextView) findViewById(R.id.tv_time);
        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 1000);
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    TIME--;
                    mTime.setText(TIME + "s");
                    if (TIME > 0) {
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);      // send message
                    } else {
                        //跳转到登录界面
                        goLogin();
                    }
            }
            super.handleMessage(msg);
        }
    };

    private void goLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
