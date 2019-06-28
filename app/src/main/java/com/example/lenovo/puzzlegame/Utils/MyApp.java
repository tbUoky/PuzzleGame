package com.example.lenovo.puzzlegame.Utils;

import android.app.Application;

import static com.example.lenovo.puzzlegame.Activity.HomeActivity.bgm_switch;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.game_state;
import static com.example.lenovo.puzzlegame.Activity.SetBgmActivity.playRingtone;
import static com.example.lenovo.puzzlegame.Activity.SetBgmActivity.ringtone;
import static com.example.lenovo.puzzlegame.Activity.SetBgmActivity.stopRingtone;

public class MyApp extends Application {
    private String Username="Usernamae";
    private String Password="Password";



    private int UserType = 1;//本地用户为1，三方用户为2
    @Override
    public void onCreate() {
        super.onCreate();
        setUsername(Username);
        setPassword(Password);
        setUserType(UserType);
        AppFrontBackHelper helper = new AppFrontBackHelper();
        helper.register(MyApp.this, new AppFrontBackHelper.OnAppStatusListener() {
            @Override
            public void onFront() {
                //应用切到前台处理
                if (ringtone != null&&bgm_switch==2&&game_state==2) {//音乐文件不为空，开关开启，且游戏进行中。
                    //播放
                    playRingtone();
                }
            }

            @Override
            public void onBack() {
                //应用切到后台处理
                if (ringtone != null&&bgm_switch==2&&game_state==2) {//音乐文件不为空，开关开启，且游戏进行中。
                   //暂停
                    stopRingtone();
                }
            }
        });
    }

    public void setUsername(String username) {
        this.Username = username;
    }
    public String getUsername() {
        return Username;
    }
    public void setPassword(String password) {
        this.Password = password;
    }
    public String getPassword() {
        return Password;
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int userType) {
        UserType = userType;
    }


}
