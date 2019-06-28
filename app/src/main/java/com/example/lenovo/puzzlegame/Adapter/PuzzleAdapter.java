package com.example.lenovo.puzzlegame.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.puzzlegame.Activity.SignupActivity;
import com.example.lenovo.puzzlegame.Beans.ImagePiece;
import com.example.lenovo.puzzlegame.Activity.HomeActivity;
import com.example.lenovo.puzzlegame.Beans.StepRecord;
import com.example.lenovo.puzzlegame.R;
import com.example.lenovo.puzzlegame.Utils.DBOpenHelper;
import com.example.lenovo.puzzlegame.Utils.GameUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.lenovo.puzzlegame.Activity.HomeActivity.Showdefault;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.game_state;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.musictype;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.recyclerView;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.stepRecordList;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.strengththenLevel;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.timerIndex;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.type;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.usepic;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.username;
import static com.example.lenovo.puzzlegame.Activity.HomeActivity.usertype;

public class PuzzleAdapter extends RecyclerView.Adapter<PuzzleAdapter.ViewHolder>  {
    //调用dbopenhelper使用
   // private Context context;

    private List<ImagePiece> mpuzzleIItemBeanList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView puzzlePic;
        TextView puzzleId;
        View PuzzleView;
        public ViewHolder (View view){
            super(view);
            PuzzleView = view;
            puzzlePic = (ImageView) view.findViewById(R.id.puzzle_pic);
            puzzleId = (TextView) view.findViewById(R.id.puzzle_id);
        }
    }

    public PuzzleAdapter(List<ImagePiece> puzzleIItemBeanList){
        mpuzzleIItemBeanList = puzzleIItemBeanList;
    }

    @NonNull
    @Override
    public PuzzleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.puzzle_item,parent,false);

        final   PuzzleAdapter.ViewHolder holder = new PuzzleAdapter.ViewHolder(view);
        holder.PuzzleView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                ImagePiece puzzleitembean = mpuzzleIItemBeanList.get(position);
               // Toast.makeText(v.getContext(),"you click view "+puzzleitembean.getIndex(),Toast.LENGTH_SHORT).show();
            }

        });
        holder.puzzlePic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
               if (game_state==1){//游戏状态为1，表示未开始，则不响应点击事件
                 return;
                }
                if (GameUtils.isMoveable(position))
                {
                    stepRecordList.add(new StepRecord(GameUtils.blankposition,position));
                 //   Toast.makeText(v.getContext(),"position : 为"+position+" 可以移动 you clicked image "+puzzleitembean.getIndex(),Toast.LENGTH_SHORT).show();
              //      Toast.makeText(v.getContext(),"白块位置为："+GameUtils.blankposition+" ;移动到的位置为 "+position,Toast.LENGTH_SHORT).show();

                    //可以移动，则交换值，更新UI
                    GameUtils.swapBlank(position,GameUtils.blankposition);
                   List<ImagePiece> imagePieceList =new ArrayList<ImagePiece>();
                   imagePieceList.addAll(GameUtils.imagePieceList);
                    Log.w("移动", "转移值  end 转移后值大小为："+imagePieceList.size());
                    GameUtils.imagePieceList.clear();
                    Log.w("移动", "清空值  end");
                   // GameUtils.imagePieceList.add(imagePieceList);
                    GameUtils.imagePieceList.addAll(imagePieceList);
                    Log.w("移动", "重赋值  end;重赋值后大小为："+GameUtils.imagePieceList.size());
                    recyclerView.smoothScrollToPosition(position);
                    recyclerView.setHasFixedSize(true);
                    HomeActivity.adapter.notifyDataSetChanged();
                    Log.w("移动", "忽略数据变化  end");
                    HomeActivity.countIndex++;
                    Log.w("移动", "步数加一  end");
                    HomeActivity.tv_count.setText(String.valueOf(HomeActivity.countIndex)+"步");
                    Log.w("移动", "步数显示  end");
                    Log.w("移动", "toast提示  end");
                   if (GameUtils.isSuccess()){
                        Toast.makeText(v.getContext(),"拼图成功！",Toast.LENGTH_LONG).show();
                        HomeActivity.soundPool.play(HomeActivity.soundMap.get(3), 1, 1, 0, 0, 1);
                       //将通关数据加入数据库

                       //调用DBOpenHelper
                       DBOpenHelper helper = new DBOpenHelper(v.getContext(),"puzzle.db",null,1);
                       SQLiteDatabase db = helper.getWritableDatabase();

                       ContentValues values= new ContentValues();
                       values.put("Game_Level",type);
                       values.put("Game_Step",HomeActivity.countIndex);
                       values.put("Username",username);
                       values.put("User_Type",String.valueOf(usertype));
                       values.put("Game_Time",timerIndex);
                       SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");//设置日期格式
                       String Over_Time = df.format(new Date());// new Date()为获取当前系统时间
                       values.put("Over_Time",Over_Time);
                       long rowid = db.insert("game_tb",null,values);

                       //通关后难度加强
                       if(type<=4)
                       {
                           type++;
                           strengththenLevel();
                       }

                    }
                    else {
                       HomeActivity.soundPool.play(HomeActivity.soundMap.get(musictype), 1, 1, 0, 0, 1);

                      }
                }

            }
        });
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull PuzzleAdapter.ViewHolder holder, int position) {
        ImagePiece puzzleIItemBean = mpuzzleIItemBeanList.get(position);
        holder.puzzlePic.setImageBitmap(puzzleIItemBean.getBitmap());
        holder.puzzleId.setText(String.valueOf(puzzleIItemBean.getIndex()));
    }

    @Override
    public int getItemCount() {
        return mpuzzleIItemBeanList.size();
    }

}
