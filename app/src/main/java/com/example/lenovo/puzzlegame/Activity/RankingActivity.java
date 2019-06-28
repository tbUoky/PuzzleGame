package com.example.lenovo.puzzlegame.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.puzzlegame.Adapter.HistoryAdapter;
import com.example.lenovo.puzzlegame.Adapter.RankAdapter;
import com.example.lenovo.puzzlegame.Beans.HistoryItem;
import com.example.lenovo.puzzlegame.Beans.RankItem;
import com.example.lenovo.puzzlegame.R;
import com.example.lenovo.puzzlegame.Utils.DBOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.username;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.usertype;

public class RankingActivity extends AppCompatActivity {

    private RecyclerView rv_rank_record;
    private List<RankItem> rankItemList = new ArrayList<>();
    private ImageView iv_none_bg;
    private  RankAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_ranking);
        rv_rank_record = (RecyclerView) findViewById(R.id.rv_rank_record);
        iv_none_bg=(ImageView)findViewById(R.id.iv_none_rank_bg);
        initRankData();//初始化排行榜
        StaggeredGridLayoutManager layoutManager;
        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    //    Toast.makeText(RankingActivity.this, " 存储值列表大小 "+rankItemList.get(0).toString(), Toast.LENGTH_SHORT).show();

        rv_rank_record.setLayoutManager(layoutManager);

        adapter = new RankAdapter(rankItemList);
        rv_rank_record.setAdapter(adapter);
        //lv_main = (ListView)
    }
    private void initRankData(){
        //根据用户名和数据类型，查出数据库游戏表中对应的游戏历史记录
        //调用DBOpenHelper
        DBOpenHelper helper = new DBOpenHelper(RankingActivity.this,"puzzle.db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Log.w("rank","排行榜数据初始化"+username+"----"+usertype);
       // Cursor c = db.rawQuery("SELECT Username,SUM(Game_Level) as Game_Level FROM game_tb WHERE Username = ? AND User_Type = ? GROUP BY User_Type,Username Order by Game_Level desc",new String[]{username,String.valueOf(usertype)});
        Cursor c = db.rawQuery("SELECT Username,SUM(Game_Level) as Game_Level FROM game_tb   GROUP BY User_Type,Username Order by Game_Level desc",null);

        //Cursor c = db.query("game_tb",new String[]{"Username","SUM(Game_Level) as Game_Level"}, null,null,new String[]{"",""},null,"_id");
      //  rankItemList.clear();

        int index = 0;
        if(c.moveToFirst()){
            rv_rank_record.setVisibility(VISIBLE);
            iv_none_bg.setVisibility(View.GONE);
            do {
                index++;
                int score = c.getInt(c.getColumnIndex("Game_Level"));
                String rank_username = c.getString(c.getColumnIndex("Username"));
                int rank_level = index;

                rankItemList.add(new RankItem(rank_username,score,rank_level));
            }while (c.moveToNext());

        }
        else
        {
            //没有数据

            iv_none_bg.setVisibility(VISIBLE);
            rv_rank_record.setVisibility(View.GONE);
            rankItemList.add(new RankItem());
        }
        Log.w("rank","排行榜数据大小"+rankItemList.size());
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
