package com.example.lenovo.puzzlegame.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.puzzlegame.Adapter.PuzzleAdapter;
import com.example.lenovo.puzzlegame.Beans.ImagePiece;
import com.example.lenovo.puzzlegame.Utils.MyApp;
import com.example.lenovo.puzzlegame.Beans.StepRecord;
import com.example.lenovo.puzzlegame.R;
import com.example.lenovo.puzzlegame.Utils.GameUtils;
import com.example.lenovo.puzzlegame.Utils.ImageSplitter;
import com.example.lenovo.puzzlegame.Utils.ImagesUtil;
import com.example.lenovo.puzzlegame.Utils.ScreenUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.lenovo.puzzlegame.Activity.SetBgmActivity.playRingtone;
import static com.example.lenovo.puzzlegame.Activity.SetBgmActivity.stopRingtone;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private MyApp myApp;//利用application传值
    //音效相关
    public static SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);

    public static HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();

    // 返回码：系统图库
    private static final int RESULT_IMAGE = 100;
    // 返回码：相机
    private static final int RESULT_CAMERA = 200;
    // IMAGE TYPE
    private static final String IMAGE_TYPE = "image/*";
    // 显示Type
    private LayoutInflater layoutInflater;//难度等级选择 用来实例化布局
    private PopupWindow popupWindow;//难度等级弹出框 可以使用任意布局的View作为其内容
    private View popupView;
    //游戏等级 3x3 4x4 5x5
    private TextView tvType3;
    private TextView tvType4;
    private TextView tvType5;
    // 游戏等级N*N
    public static int type = 3;
    private TextView tv_puzzle_main_type_selected;
    //默认的显示的难度等级的textview控件


//拼图相关
    public static Bitmap usepic;
    private  static  List<ImagePiece> imagePieceList= null;
    public static   RecyclerView recyclerView;
    public static StaggeredGridLayoutManager layoutManager;
    // 计时器类
    public static Timer timer;
    // 计时器
    public static TextView tv_Timer;
    // 计时显示
    public static int timerIndex = 0;//时间计时
    // 步数显示
    public static TextView tv_count ;
    public static int countIndex = 0; //步数

    public  static  PuzzleAdapter adapter;

    private  static  FloatingActionButton fab;
    //悬浮按钮，初始不可见，开始游戏后，可见，点击即可隐藏游戏相关按钮看到原图，再点击即可显示按钮隐藏原图
    public static int game_state ;//判断拼图子项是否可以响应点击事件
    //游戏进行状态为 2 ；未开始为1
    public  static int screenWidth ;
    public  static int screenHeigt ;
    //选择本地图片时，
    private  Uri imageUri;
    public static int musictype = 0 ;//音效索引

    //后退相关
    //将游戏步骤记录进去，后退时从中取出，依次还原
    public static List<StepRecord> stepRecordList = new ArrayList<StepRecord>();

    //游戏记录相关
    public static int usertype;
  //  public static int gameflage;//未通关，设置为1，通关，设置为2
    public static String username ;

    public static  int backindex ;//程序退出 1正常退出 2 异常退出

    //游戏背景音乐
    public static int bgm_switch ;//如果为1，表示背景音乐开关选择关，如果为2，表示背景音乐开关为开
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        load();
//        if (savedInstanceState!=null){
//            int music_type = savedInstanceState.getInt("musictype");
//            Log.w("进程回收数据恢复","恢复数据musictype："+music_type);
//        }

        //获取屏幕宽高
         screenWidth =  ScreenUtil.getDisplayMetrics(this).widthPixels;
         screenHeigt = ScreenUtil.getDisplayMetrics(this).heightPixels;
         //声明recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
         //音效资源加载，哈希Map
        soundMap.put(0, soundPool.load(HomeActivity.this,R.raw.m001, 1));
        soundMap.put(1, soundPool.load(HomeActivity.this,R.raw.m002, 1));
        soundMap.put(2, soundPool.load(HomeActivity.this, R.raw.m003, 1));
        soundMap.put(3, soundPool.load(HomeActivity.this, R.raw.m004, 1));
        soundMap.put(4, soundPool.load(HomeActivity.this, R.raw.m005, 1));

       // Item it = (Item) R.findR.id.nav_camera

        myApp = (MyApp) getApplication();
        username = myApp.getUsername();//获取用户名
        usertype = myApp.getUserType();//获取用户类型，后续插入到数据库中需要
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //初始不可见，开始游戏后，可见，点击即可隐藏游戏相关按钮看到原图，再点击即可显示按钮隐藏原图
                LinearLayout ll_puzzle_run_btns = (LinearLayout)  findViewById(R.id.ll_puzzle_run_btns);
                LinearLayout ll_puzzle_main_img = (LinearLayout)  findViewById(R.id.ll_puzzle_main_img);
                ImageView imageView = (ImageView)findViewById(R.id.iv_puzzle_show);
                imageView.setImageBitmap(usepic);
                if(ll_puzzle_run_btns.getVisibility() ==View.VISIBLE)
                {
                    //隐藏按钮，显示图片
                    ll_puzzle_run_btns.setVisibility(View.GONE);
                    ll_puzzle_main_img.setVisibility(View.VISIBLE);
                }
                else{
                    //隐藏图片，显示按钮
                    ll_puzzle_main_img.setVisibility(View.GONE);
                    ll_puzzle_run_btns.setVisibility(View.VISIBLE);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headView = navigationView.getHeaderView(0);//声明 控件
        TextView tv_header_username = headView.findViewById(R.id.tv_header_username);//声明 控件
        tv_header_username.setText(username);// 给控件赋值用户名
        navigationView.setNavigationItemSelectedListener(this);

        tv_count = (TextView)findViewById(R.id.tv_puzzle_main_counts) ;
        /**
         * 设置默认图片
         */
        usepic = BitmapFactory.decodeResource(getResources(), R.drawable.pic1);
        Showdefault(usepic,type);
        /**
         * 选择难度等级level，相关准备
         */
        choselevelinit();
        /**
         * 显示难度Type
         */
        tv_puzzle_main_type_selected.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 弹出popup window
                popupShow(v);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
         //   super.onBackPressed();
            exit();

        }
    }
    private long exitTime = 0;
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_history_record) {
        //    Toast.makeText(HomeActivity.this, "游戏历史记录页面", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.menu_rank_record) {
            //跳转到排行榜页面
          //  Toast.makeText(HomeActivity.this, "排行榜显示", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, RankingActivity.class);
            startActivity(intent);

        }  else if (id == R.id.menu_set_music) {
          //  Toast.makeText(HomeActivity.this, "设置音效", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, SetMusicActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_set_bgm) {
          //  Toast.makeText(HomeActivity.this, "设置背景音乐", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, SetBgmActivity.class);
            startActivity(intent);
        }else if (id == R.id.menu_about){
            Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_exit_game) {
            //退出
            Log.w("进程","点击侧滑菜单里的退出，应用销毁");
            backindex=1;//点击退出按钮退出，为正常退出
            save();
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //选择难度等级相关
    private void choselevelinit(){
        //难度等级
        tv_puzzle_main_type_selected = (TextView) findViewById(R.id.tv_puzzle_main_type_selected);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        // type view
        popupView = layoutInflater.inflate(R.layout.level_selected_layout, null);
        tvType3 = (TextView) popupView.findViewById(R.id.btn_home_level1);
        tvType4 = (TextView) popupView.findViewById(R.id.btn_home_level2);
        tvType5 = (TextView) popupView.findViewById(R.id.btn_home_level3);
        // 监听事件
        tvType3.setOnClickListener(this);
        tvType4.setOnClickListener(this);
        tvType5.setOnClickListener(this);
    }

    /**
     * popup window item点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Type
            case R.id.btn_home_level1:
                type = 3;
                Showdefault(usepic,type);
                tv_puzzle_main_type_selected.setText("3 X 3");
                break;
            case R.id.btn_home_level2:
                type = 4;
                Showdefault(usepic,type);
                tv_puzzle_main_type_selected.setText("4 X 4");
                break;
            case R.id.btn_home_level3:
                type = 5;
                Showdefault(usepic,type);
                tv_puzzle_main_type_selected.setText("5 X 5");
                break;
            default:
                break;
        }
        popupWindow.dismiss();
    }


    /**
     * 显示popup window
     *
     * @param view
     */
    private void popupShow(View view) {
        // 显示popup window
        popupWindow = new PopupWindow(popupView,800, 160);
        popupWindow.setFocusable(true);//设置为true，popupWindow内容区域内才能获取控件的点击事件
        popupWindow.setOutsideTouchable(true);//点击外部，消失弹窗
        // 透明背景
        Drawable transpent = new ColorDrawable(Color.TRANSPARENT);
        popupWindow.setBackgroundDrawable(transpent);
        // 获取位置
        int[] location = new int[2];
        view.getLocationOnScreen(location);//View左上顶点在屏幕中的绝对位置.(屏幕范围包括状态栏).
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (location[0] + view.getWidth() / 2) - 400, location[1] +100);

    }


    /**
     * UI更新Handler
     */
    public static Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 更新计时器
                    timerIndex++;
                    tv_Timer.setText("" + timerIndex+"秒");
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 计时器线程
     */
    public static TimerTask timerTask;


    /**
     * 对图片处理 自适应大小
     *
     * @param bitmap
     */
    private static void handlerImage(Bitmap bitmap,int pieces) {
        // 将图片放大到固定尺寸

        Log.w("handlerImage", "根据实际显示大小调整图片start");
        usepic = new ImagesUtil().resizeBitmap(screenWidth * 0.8f<screenHeigt * 0.6f?screenWidth * 0.8f:screenHeigt * 0.6f, screenWidth * 0.8f<screenHeigt * 0.6f?screenWidth * 0.8f:screenHeigt * 0.6f, bitmap);
        //      usepic = new ImagesUtil().resizeBitmap(screenWidth * 0.8f, screenHeigt * 0.6f, bitmap);
        Log.w("handlerImage", "根据实际显示大小调整图片end");
        Log.w("handlerImage", "图片切割start");
        imagePieceList = ImageSplitter.split(usepic,pieces,pieces);

        Log.w("handlerImage", "图片切割end");
    }
    /*
     * 默认页面，将Drawable中某个图片设置为默认拼图图片，将该图片转为bitmap，然后按屏幕大小缩放，；分割为n x n，转为List。
     * */
    public static void Showdefault(Bitmap usebitmap,int pieces){
        handlerImage(usebitmap,pieces);
        Log.w("onCreate", "声明recyclerview  end");
        adapter = new PuzzleAdapter(GameUtils.imagePieceList);
        Log.w("onCreate", "声明适配器  end");
        layoutManager = new StaggeredGridLayoutManager(pieces, StaggeredGridLayoutManager.VERTICAL);
         Log.w("onCreate", "声明StaggeredGridLayoutManager  end");
        recyclerView.setLayoutManager(layoutManager);
        Log.w("onCreate", "设置setLayoutManager  end");
        recyclerView.setAdapter(adapter);
        Log.w("onCreate", "设置适配器  end");
        game_state=1;
    }
    /**
     * 开始按钮：
     * 1.打乱并显示
     * 2.定时器显示并开启
     * 3.步数textview显示（后续交换按钮时，需要开启）
     * 4.隐藏难度等级的显示
     * 5.隐藏开始，本地的 按钮
     * 6.显示结束按钮
     * 7.显示后退按钮
     */
    public void Play(View v){
        //游戏音乐
        if (bgm_switch==2){
            Log.w("开关按钮", "开关开启，游戏开始，则开始播放：------------");
            playRingtone();
        }

        fab.setVisibility(View.VISIBLE);
        game_state=2;
        GameUtils.Sort(HomeActivity.type);
        Log.w("Play", "imagePieceList的size    :" +GameUtils.imagePieceList.size());

        adapter = new PuzzleAdapter(GameUtils.imagePieceList);
        recyclerView.setAdapter(adapter);

        LinearLayout ll_home_spinner = (LinearLayout)  findViewById(R.id.ll_home_spinner);
        ll_home_spinner.setVisibility(View.VISIBLE);

        LinearLayout ll_puzzle_run_btns = (LinearLayout)  findViewById(R.id.ll_puzzle_run_btns);
        ll_puzzle_run_btns.setVisibility(View.VISIBLE);

        LinearLayout ll_home_level = (LinearLayout)  findViewById(R.id.ll_home_level);
        ll_home_level.setVisibility(View.GONE);

        LinearLayout ll_puzzle_main_btns = (LinearLayout)  findViewById(R.id.ll_puzzle_main_btns);
        ll_puzzle_main_btns.setVisibility(View.GONE);

        //定时器显示并开启
        // TV计时器
        tv_Timer = (TextView) findViewById(R.id.tv_puzzle_main_time);
        tv_Timer.setText("0秒");
        timer = new Timer(true);
        // 计时器线程
        timerTask = new TimerTask() {

            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
        // 每1000ms执行 延迟0s
        timer.schedule(timerTask, 0, 1000);
    }
    /**
     * 本地按钮：
     * 1. 调用本地相册，选用图片
     */
    public void LocalPic(View v){
        showDialogCustom();
    }

    /**
     * 后退按钮：
     * 1. 从BackList中后退
     */
    public void GameBack(View v){
        if (stepRecordList.size()>0) {
            StepRecord back ;
            back = stepRecordList.get(stepRecordList.size() - 1);
            //交换位置，并移除后一项。
            stepRecordList.remove(stepRecordList.size() - 1);
            GameUtils.swapBlank(back.getBlankfrom(),back.getBlankgo());
            List<ImagePiece> imagePieceList =new ArrayList<ImagePiece>();
            imagePieceList.addAll(GameUtils.imagePieceList);
            GameUtils.imagePieceList.clear();
            // GameUtils.imagePieceList.add(imagePieceList);
            GameUtils.imagePieceList.addAll(imagePieceList);
//            recyclerView.smoothScrollToPosition(position);
//            recyclerView.setHasFixedSize(true);
            HomeActivity.adapter.notifyDataSetChanged();
            HomeActivity.countIndex++;
            HomeActivity.tv_count.setText(String.valueOf(HomeActivity.countIndex)+"步");
            HomeActivity.soundPool.play(HomeActivity.soundMap.get(musictype), 1, 1, 0, 0, 1);

        }
        else {
            Toast.makeText(v.getContext(),"无可回退步骤记录",Toast.LENGTH_LONG).show();
            HomeActivity.soundPool.play(HomeActivity.soundMap.get(4), 1, 1, 0, 0, 1);

        }
        //  GameUtils.Sort(HomeActivity.type);
//        PuzzleAdapter adapter = new PuzzleAdapter(GameUtils.imagePieceList);
//        recyclerView.setAdapter(adapter);
    }

    /**
     * 结束按钮：
     * 1. 结束游戏，恢复默认页面
     * 2.定时器结束，初始化步数和时间的值
     * 3.步数和时间的控件，显示原图的悬浮按钮隐藏
     * 4.显示难度等级
     */
    public void GameEnd(View v){
        stepRecordList.clear();//可后退步骤记录清空
        if (bgm_switch==2){
            Log.w("开关按钮", "开关开启，游戏结束，则暂停：------------");
            stopRingtone();
        }
      //  usepic = BitmapFactory.decodeResource(getResources(), R.drawable.pic1);
        fab.setVisibility(View.INVISIBLE);
        type = 3;
        Showdefault(usepic,type);
        countIndex = 0;
        timerIndex=0;
        tv_count.setText(String.valueOf(0)+"步");
        tv_Timer.setText(String.valueOf(0)+"秒");
        timer.cancel();
        timerTask.cancel();

        LinearLayout ll_home_spinner = (LinearLayout)  findViewById(R.id.ll_home_spinner);
        ll_home_spinner.setVisibility(View.GONE);

        LinearLayout ll_puzzle_run_btns = (LinearLayout)  findViewById(R.id.ll_puzzle_run_btns);
        ll_puzzle_run_btns.setVisibility(View.GONE);

        LinearLayout ll_home_level = (LinearLayout)  findViewById(R.id.ll_home_level);
        ll_home_level.setVisibility(View.VISIBLE);

        LinearLayout ll_puzzle_main_btns = (LinearLayout)  findViewById(R.id.ll_puzzle_main_btns);
        ll_puzzle_main_btns.setVisibility(View.VISIBLE);

        tv_puzzle_main_type_selected.setText("3 X 3");

    }

    // 显示选择系统图库 相机对话框
    private void showDialogCustom() {
        // 本地图册、相机选择
        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

        String[] customItemArray = new String[]{"本地图册", "相机拍照"};
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                HomeActivity.this);
        dialogBuilder.setTitle("选择：");
        dialogBuilder.setItems(customItemArray,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0) {
                            // 本地图册
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK, null);
                            intent.setDataAndType(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    IMAGE_TYPE);
                            startActivityForResult(intent, RESULT_IMAGE);
                        } else if (which==1) {

                            File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                            try {
                                if (outputImage.exists()){
                                    outputImage.delete();
                                }
                                outputImage.createNewFile();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            if (Build.VERSION.SDK_INT>=24){
                               imageUri = FileProvider.getUriForFile(HomeActivity.this,
                                       "com.example.lenovo.puzzlegame.fileproviders",outputImage);
                            }
                            else {
                                imageUri = Uri.fromFile(outputImage);

                            }
                            // 系统相机
                            Intent intent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(
                                    MediaStore.EXTRA_OUTPUT,
                                    imageUri);
                            startActivityForResult(intent, RESULT_CAMERA);
                        }
                    }
                });
        dialogBuilder.create().show();
    }

    /**
     * 调用图库相机回调方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.w("回调","调用回调函数");
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photoSelectTemp=null;
        if (resultCode == RESULT_OK) {
            Log.w("回调","调用回调函数ok");
            if (requestCode == RESULT_IMAGE && data != null) {
                // 相册
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                photoSelectTemp = BitmapFactory.decodeFile(picturePath);
                if (photoSelectTemp==null)
                {
                    Log.w("回调","调用相册为空ok"+BitmapFactory.decodeFile(picturePath));
                }else {
                    Log.w("回调", "调用相册不为空ok" + photoSelectTemp);
                    Showdefault(photoSelectTemp,type);
                }

            } else if (requestCode == RESULT_CAMERA) {

                try {
                    //将拍摄的照片显示出来
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    Showdefault(bitmap,type);
                }
                catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        }
    }

    //难度加强

    public static void strengththenLevel(){
        //难度加强
        Showdefault(usepic,type);
        countIndex = 0;
        timerIndex=0;
        tv_count.setText(String.valueOf(0)+"步");
        tv_Timer.setText(String.valueOf(0)+"秒");
        timer.cancel();
        timerTask.cancel();
        timer = new Timer(true);
        // 计时器线程
        timerTask = new TimerTask() {

            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
        // 每1000ms执行 延迟0s
        timer.schedule(timerTask, 0, 1000);
    }

//    @Override
//  protected void onSaveInstanceState(Bundle outsState){
//        super.onSaveInstanceState(outsState);
//        int music_type = musictype;
//        outsState.putInt("musictype",music_type);
//        Log.w("进程保存","音效索引值:"+music_type);
//      //musictype
//  }
@Override
protected void onDestroy(){
    Log.w("进程销毁","kill，掉进程，拼图页面activity销毁，此时音效索引值为:"+musictype);
    backindex=2;//后台kill退出程序
    save();
    super.onDestroy();
}
public void save( ){
        String music_type = String.valueOf(musictype);
        String back_index= String.valueOf(backindex);
    FileOutputStream out = null;
    BufferedWriter writer = null;
    try{
        out = openFileOutput("data", Context.MODE_PRIVATE);
        writer = new BufferedWriter(new OutputStreamWriter(out));
        writer.write(music_type+","+back_index);
    }catch (IOException e){
        e.printStackTrace();
    }
    finally {
        try
        {
            if (writer!=null)
            {
                writer.close();
            }

        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

public void load() {
    FileInputStream in = null;
    BufferedReader reader = null;
    StringBuilder content = new StringBuilder();
    try {
        in = openFileInput("data");
        reader = new BufferedReader(new InputStreamReader(in));
        String line = "";
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String save_data = content.toString();
    Log.w("进程", "从文件中读取数据:" + save_data);
    try {
        String[] s=save_data.split(",");
        musictype = Integer.valueOf(s[0]).intValue();
        backindex = Integer.valueOf(s[1]).intValue();

    } catch (Exception e) {
        e.printStackTrace();
    }

}
}

