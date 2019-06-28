package com.example.lenovo.puzzlegame.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.puzzlegame.Beans.HistoryItem;
import com.example.lenovo.puzzlegame.R;

import java.util.List;

public class HistoryAdapter  extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>  {
    private List<HistoryItem> mhistoryItemList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_history_item_game_level;
        TextView tv_history_item_game_step;
        TextView      tv_history_item_game_use_time;
        TextView  tv_history_item_game_end_time;
        public ViewHolder(View view){
            super(view);
            tv_history_item_game_level=(TextView)view.findViewById(R.id.tv_history_item_game_level);
            tv_history_item_game_step=(TextView)view.findViewById(R.id.tv_history_item_game_step);
            tv_history_item_game_use_time=(TextView)view.findViewById(R.id.tv_history_item_game_use_time);
            tv_history_item_game_end_time=(TextView)view.findViewById(R.id.tv_history_item_game_end_time);

        }
    }
    public HistoryAdapter(List<HistoryItem> historyItemList){
        mhistoryItemList = historyItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryItem historyItem = mhistoryItemList.get(position);
        holder.tv_history_item_game_level.setText(historyItem.getGame_Level());
        holder.tv_history_item_game_step.setText(historyItem.getGame_Step());
        holder.tv_history_item_game_end_time.setText(historyItem.getOver_Time());
        holder.tv_history_item_game_use_time.setText(historyItem.getGame_Time());
    }

    @Override
    public int getItemCount() {
        return mhistoryItemList.size();
    }
}
