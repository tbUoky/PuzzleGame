package com.example.lenovo.puzzlegame.Activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.puzzlegame.Utils.MyApp;
import com.example.lenovo.puzzlegame.R;
import com.example.lenovo.puzzlegame.Utils.DBOpenHelper;
import com.example.lenovo.puzzlegame.Utils.JellyInterpolator;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private MyApp myApp;
    private TextView mBtnLogin;
    private View progress;
    private View mInputLayout;
    private float mWidth, mHeight;
    private LinearLayout mName, mPsw;

    private Tencent mTencent;
    private LoginListener mListener;
    private static final String APP_ID = "1108801123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myApp = (MyApp) getApplication();
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());


        mListener = new LoginListener();
        initView();//登录动画的初始化
    }
    public void loginQQ(View view) {
        if (!mTencent.isSessionValid()) {
            /**
             * 第一个参数为Activity
             * 第二个参数指明应用需要获得哪些API的权限，由“，”分隔。
             例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
             * 第三个参数为回调
             */
            mTencent.login(LoginActivity.this, "all", mListener);
        }
    }

    /**
     * QQ登录授权回调
     */
    private class LoginListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            Toast.makeText(LoginActivity.this, "授权成功！", Toast.LENGTH_SHORT).show();
            JSONObject obj = (JSONObject) o;
            //  resultView.setText(obj.toString());
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                // 设置授权成功返回来的信息，如openid，token等，以后就可以用mTencent获取用户信息
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                // 获取QQ用户信息
                QQToken qqToken = mTencent.getQQToken();
                final UserInfo mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        //   Log.e(getClass().getName(), "登录成功" + response.toString());
                        JSONObject objuer = (JSONObject) response;

                        try {
                            String xxxxxx = objuer.getString("nickname");
                            String userHeard1 = objuer.getString("figureurl_qq_2");

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            ImageView img = (ImageView) findViewById(R.id.loginpage_logo);
                            myApp = (MyApp) getApplication();
                            myApp.setUsername(xxxxxx);
                            myApp.setUserType(2);//三方登录，设置用户类型为2
                          //  myApp.setUserpic(R.drawable.defaultpic);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(getClass().getName(), "登录失败" + uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(getClass().getName(), "登录取消");
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "授权出错！", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消！", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 想在Activity中得到新打开Activity关闭后返回的数据，
     * 需要使用系统提供的startActivityForResult(Intent intent,int requestCode)方法打开新的Activity，
     * 新的Activity关闭后会向前面的Activity传回数据，为了得到传回的数据，
     * 必须在前面的Activity中重写onActivityResult(int requestCode, int resultCode,Intent data)方法：
     * 非常重要，要不然回调不了
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, mListener);
    }

    public void CheckLogin() {
//判断账号/密码是否有输入的处理...
        String username="";
        String pwd="";
        EditText et_login_username =(EditText)findViewById(R.id.et_login_username);
        EditText et_login_password =(EditText)findViewById(R.id.et_login_password);
        username = et_login_username.getText().toString();
        pwd = et_login_password.getText().toString();
        if (android.text.TextUtils.isEmpty(username) || android.text.TextUtils.isEmpty(pwd)) {//如果有空值
            Toast.makeText(this,"用户名与密码皆不能为空", Toast.LENGTH_SHORT).show();
            recovery();
            return ;
        }
        //调用DBOpenHelper （qianbao.db是创建的数据库的名称）
        DBOpenHelper helper = new DBOpenHelper(this,"puzzle.db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        //根据画面上输入的账号/密码去数据库中进行查询（user_tb是表名）
        Cursor c = db.query("user_tb",null,"Username=? and Password=?",new String[]{username,pwd},null,null,null);
        //如果有查询到数据
        if(c!=null && c.getCount() >= 1){
            //可以把查询出来的值打印出来在后台显示/查看
        /*String[] cols = c.getColumnNames();
        while(c.moveToNext()){
            for(String ColumnName:cols){
                Log.i("info",ColumnName+":"+c.getString(c.getColumnIndex(ColumnName)));
            }
        }*/
            myApp.setUsername(username);
            myApp.setUserType(1);//本地用户，用户类别为1
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

            startActivity(intent);
            c.close();
            db.close();
            this.finish();
        }
        //如果没有查询到数据
        else{
            Toast.makeText(this, "用户名或密码输入错误！", Toast.LENGTH_SHORT).show();
            recovery();
        }
    }

    public void GoSignup(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    public void ExitSLK(View v) {
        //退出程序
        //  LoginPageA.this.finish();
        System.exit(0);
    }

    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.login_btn);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.loginpagea_input);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 隐藏输入框，开启动画
                mWidth = mBtnLogin.getMeasuredWidth();// getMeasuredWidth :返回最后一次调用onMeasure所测量得到的宽度
                mHeight = mBtnLogin.getMeasuredHeight();// getMeasuredHeight ：返回的是原始测量高度
                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);
                inputAnimator(mInputLayout, mWidth, mHeight);//传入输入的布局，宽，高

            }
        });
    }

    /**
     * 输入框的动画效果
     *
     * @param view 控件
     * @param w    宽
     * @param h    高
     */
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);
                CheckLogin();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }

    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view, animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }

    /**
     * 恢复初始状态
     */
    private void recovery() {

        progress.setVisibility(View.GONE);
        mInputLayout.clearAnimation();
        progress.clearAnimation();
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mInputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        mInputLayout.setLayoutParams(params);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 0.5f, 1f);
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
        //   animator2.end();
        mInputLayout.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mPsw.setVisibility(View.VISIBLE);
    }

}
