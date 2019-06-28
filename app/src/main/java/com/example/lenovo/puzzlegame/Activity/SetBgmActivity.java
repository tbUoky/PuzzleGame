package com.example.lenovo.puzzlegame.Activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.lenovo.puzzlegame.R;

import java.lang.reflect.Field;

import static com.example.lenovo.puzzlegame.Activity.HomeActivity.backindex;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.bgm_switch;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.game_state;

public class SetBgmActivity extends    AppCompatPreferenceActivity {

    public static Ringtone ringtone;
    private static SwitchPreference switchPreference;
    //重复播放相关
    public static Class<Ringtone> clazz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.activity_set_bgm);
        addPreferencesFromResource(R.xml.setting_bgm);
        RingtonePreference test = (RingtonePreference) findPreference("ringtone_bgm");
        bindPreferenceSummaryToValue(findPreference("ringtone_bgm"));

        switchPreference = (SwitchPreference) findPreference("switch_bgm");
        Log.w("进程","读取backindex的值"+backindex);
        if (backindex==1||backindex==2){
            Log.w("进程","上次退出程序，初始化switch"+backindex);
            switchPreference.setChecked(false);
            backindex=0;
        }
       // switchPreference.setChecked(false);
        switchPreference.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference,
                                                      Object newValue) {
                        boolean switched = ((SwitchPreference) preference)
                                .isChecked();
                        if (!switched) {

//                            if (ringtone != null) {
//                                Log.w("开关按钮", "不为空，播放：------------");
//                                playRingtone();
//
//                            }
                            bgm_switch=2;
                            Log.w("开关按钮", "开关开启");
                            if (ringtone != null&&game_state==2) {//音乐文件不为空，且游戏进行中。
                                Log.w("开关按钮", "选中则播放：------------");
                                playRingtone();
                            }
                            else
                            {
                                Log.w("开关按钮", "开关开启，但未开始游戏或音乐文件为空");
                            }
                        } else {
                            bgm_switch=1;
                            Log.w("开关按钮", "开关关闭");
                            if (ringtone != null&&game_state==2) {//音乐文件不为空，且游戏进行中。
                                Log.w("开关按钮", "未选中，则暂停：------------");
                                stopRingtone();
                            }
                            else {
                                Log.w("开关按钮", "开关关闭，但未开始游戏或音乐文件为空");
                            }
//                            Log.w("开关按钮", "未选中则暂停：------------");
//                            if (ringtone != null) {
//                                Log.w("开关按钮", "不为空，暂停：------------");
//                               // ringtone.stop();
//                                stopRingtone();
//                            }
                        }
                        return true;
                    }

                }
        );

        setupActionBar();
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary("待选");

                } else {
                    ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    Log.w("开关按钮", stringValue + "------------");

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.

                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                        //stringValue
                     //   setRingtoneRepeat(ringtone);
                        //    ringtone.play();
                      //  playRingtone();//选中新的文件，播放
                    }

                }

            }
            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

//    @Override
//    public void onTrimMemory(int level) {
//        super.onTrimMemory(level);
//        if (level == TRIM_MEMORY_UI_HIDDEN) {
//            Log.e("切到后台", "切到后台了..音乐暂停");
//            if (ringtone != null) {
//                ringtone.stop();
//            }
//        }
//    }

    //重复播放音乐
    public static void setRingtoneRepeat(Ringtone rt) {
        Class<Ringtone> clazz = Ringtone.class;
        try {
            Field audio = clazz.getDeclaredField("mLocalPlayer");
            audio.setAccessible(true);
            MediaPlayer target = (MediaPlayer) audio.get(rt);
            target.setLooping(true);
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //播放铃声
    public static void playRingtone() {
// TODO Auto-generated method stub
        Log.w("开关按钮", "音乐播放：------------");
        ringtone.setStreamType(AudioManager.STREAM_RING);//因为rt.stop()使得MediaPlayer置null,所以要重新创建（具体看源码）
        setRingtoneRepeat(ringtone);//设置重复提醒
        ringtone.play();
    }

    //停止铃声
    public static void stopRingtone() {
// TODO Auto-generated method stub
        Log.w("开关按钮", "音乐暂停：------------");
        ringtone.stop();
    }
}
