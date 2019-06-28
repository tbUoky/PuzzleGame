package com.example.lenovo.puzzlegame.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.lenovo.puzzlegame.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        TextView tv_about = (TextView)findViewById(R.id.tv_about_introduce);
        tv_about.setText("    用户排行按照所有通关的游戏等级之和从高到底排序，没有通关，则不计入排名；" +
                "用户每一次通关，都会计入游戏记录；" +
                "用户可以设置拼块的移动音效和游戏背景音乐；" +
                "用户可以使用默认图片、相册、调用摄像头拍摄的图片进行拼图；" +
                "用户点击页面上的难度等级，会弹出难度选择框，可以选择3X3，4X4，5X5；" +
                "用户点击开始按钮，可以开始进行拼图游戏，页面上会显示步数、所用时间、后退按钮、结束按钮、悬浮按钮，点击悬浮按钮，即可显示原图，再点击即隐藏原图。");
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
