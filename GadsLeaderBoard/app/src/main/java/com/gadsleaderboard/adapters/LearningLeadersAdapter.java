package com.gadsleaderboard.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gadsleaderboard.R;
import com.gadsleaderboard.models.LearningLeader;

import java.util.ArrayList;

public class LearningLeadersAdapter extends RecyclerView.Adapter<LearningLeadersAdapter.ViewHolder> {
    private ArrayList<LearningLeader> mLearningLeaders = new ArrayList<>();

    @NonNull
    @Override
    public LearningLeadersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LearningLeadersAdapter.ViewHolder holder, int position) {
        LearningLeader learningLeader = mLearningLeaders.get(position);
        holder.leaderName.setText(learningLeader.name);
        String leaderInfo = learningLeader.hours + " Watching hours, " + learningLeader.country;
        holder.leaderInfo.setText(leaderInfo);
        Glide.with(holder.mView.getContext()).load(learningLeader.badgeUrl).into(holder.leaderBadge);
    }

    @Override
    public int getItemCount() {
        return mLearningLeaders.size();
    }

    public void changeData(ArrayList<LearningLeader> newData){
        mLearningLeaders = newData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView leaderBadge;
        public TextView leaderName, leaderInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            leaderBadge = itemView.findViewById(R.id.leader_badge);
            leaderName = itemView.findViewById(R.id.tvLeaderName);
            leaderInfo = itemView.findViewById(R.id.tvLeaderInfo);
        }
    }
}
