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
import com.gadsleaderboard.models.SkillIqLeader;

import java.util.ArrayList;

public class SkillLeadersAdapter extends RecyclerView.Adapter<SkillLeadersAdapter.ViewHolder> {
    private ArrayList<SkillIqLeader> mSkillIqLeaders = new ArrayList<>();
    @NonNull
    @Override
    public SkillLeadersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillLeadersAdapter.ViewHolder holder, int position) {
        SkillIqLeader learningLeader = mSkillIqLeaders.get(position);
        holder.leaderName.setText(learningLeader.name);
        String leaderInfo = learningLeader.score + " Watching hours, " + learningLeader.country;
        holder.leaderInfo.setText(leaderInfo);
        Glide.with(holder.mView.getContext()).load(learningLeader.badgeUrl).into(holder.leaderBadge);
    }

    @Override
    public int getItemCount() {
        return mSkillIqLeaders.size();
    }

    public void changeData(ArrayList<SkillIqLeader> newData) {
        mSkillIqLeaders = newData;
        notifyDataSetChanged();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
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
