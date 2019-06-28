package com.example.lenovo.puzzlegame.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.puzzlegame.Beans.HistoryItem;
import com.example.lenovo.puzzlegame.Beans.RankItem;
import com.example.lenovo.puzzlegame.R;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private List<RankItem> mrankItemList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_rank_item_level;
        TextView tv_rank_item_username;
        TextView tv_rank_item_score;
        public ViewHolder(View view){
            super(view);
            tv_rank_item_level=(TextView)view.findViewById(R.id.tv_rank_item_level);
            tv_rank_item_username=(TextView)view.findViewById(R.id.tv_rank_item_username);
            tv_rank_item_score=(TextView)view.findViewById(R.id.tv_rank_item_score);

        }
    }
    public RankAdapter(List<RankItem> rankItemList){
        mrankItemList = rankItemList;
    }

    @NonNull
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item,parent,false);
        RankAdapter.ViewHolder holder = new RankAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RankAdapter.ViewHolder holder, int position) {
        RankItem rankItem = mrankItemList.get(position);
        holder.tv_rank_item_level.setText(String.valueOf(rankItem.getRank_level()));
        holder.tv_rank_item_score.setText(String.valueOf(rankItem.getScore()));
        holder.tv_rank_item_username.setText(rankItem.getUsername());
    }

    @Override
    public int getItemCount() {
        return mrankItemList.size();
    }
}
