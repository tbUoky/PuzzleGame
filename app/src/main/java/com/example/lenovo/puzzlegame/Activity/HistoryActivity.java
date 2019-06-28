package com.example.lenovo.puzzlegame.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.puzzlegame.Adapter.HistoryAdapter;
import com.example.lenovo.puzzlegame.Beans.HistoryItem;
import com.example.lenovo.puzzlegame.R;
import com.example.lenovo.puzzlegame.Utils.DBOpenHelper;
import com.example.lenovo.puzzlegame.Utils.ImagesUtil;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.screenHeigt;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.screenWidth;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.username;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.usertype;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView rv_history_record;
    private List<HistoryItem> historyItemList = new ArrayList<>();
    private ImageView iv_none_bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_history);
        rv_history_record = (RecyclerView) findViewById(R.id.rv_history_record);
        iv_none_bg=(ImageView)findViewById(R.id.iv_none_history_bg);
        initHistoryData();//初始化游戏历史记录
      //  LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        StaggeredGridLayoutManager layoutManager;
        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        //Log.w("onCreate", "声明StaggeredGridLayoutManager  end");
        //recyclerView.setLayoutManager(layoutManager);

        rv_history_record.setLayoutManager(layoutManager);
        HistoryAdapter adapter = new HistoryAdapter(historyItemList);
        rv_history_record.setAdapter(adapter);
    }

    private void initHistoryData(){
        //根据用户名和数据类型，查出数据库游戏表中对应的游戏历史记录
        //调用DBOpenHelper
        DBOpenHelper helper = new DBOpenHelper(HistoryActivity.this,"puzzle.db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();


        //根据 用户名和用户类型 去数据库中进行查询
        Cursor c = db.query("game_tb",null,"Username=? and User_Type= ? ",new String[]{username,String.valueOf(usertype)},null,null,"_id");
       // historyItemList.add(new HistoryItem("级别","步数","用时/s","结束时间"));
        if(c.moveToFirst()){

            do {
                rv_history_record.setVisibility(VISIBLE);
                iv_none_bg.setVisibility(View.GONE);
                int game_level = c.getInt(c.getColumnIndex("Game_Level"));
                int game_time = c.getInt(c.getColumnIndex("Game_Time"));
                int game_step = c.getInt(c.getColumnIndex("Game_Step"));
                String over_time = c.getString(c.getColumnIndex("Over_Time"));
                HistoryItem historyItem = new HistoryItem(game_level+"",game_step+"",game_time+"",over_time);
                historyItemList.add(historyItem);
            }while (c.moveToNext());
        }
        else
        {
            //没有数据

            iv_none_bg.setVisibility(VISIBLE);
            rv_history_record.setVisibility(View.GONE);
        }
        c.close();
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
