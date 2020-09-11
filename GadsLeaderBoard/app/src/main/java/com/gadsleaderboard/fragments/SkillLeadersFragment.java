package com.gadsleaderboard.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.gadsleaderboard.utils.ApiEndPoints;
import com.gadsleaderboard.R;
import com.gadsleaderboard.utils.RetrofitBuilder;
import com.gadsleaderboard.models.SkillIqLeader;
import com.gadsleaderboard.adapters.SkillLeadersAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SkillLeadersFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SkillLeadersAdapter mLeadersAdapter;
    private ProgressBar mProgressBar;

    public SkillLeadersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_skill_leaders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler_skill_leaders);
        mProgressBar = view.findViewById(R.id.progressBar);
        mLeadersAdapter = new SkillLeadersAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setAdapter(mLeadersAdapter);

        mProgressBar.setVisibility(View.VISIBLE);
        ApiEndPoints service = RetrofitBuilder.buildSerVice(ApiEndPoints.class);
        Call<ArrayList<SkillIqLeader>> call = service.getSkillLeaders();

        call.enqueue(new Callback<ArrayList<SkillIqLeader>>() {
            @Override
            public void onResponse(Call<ArrayList<SkillIqLeader>> call, Response<ArrayList<SkillIqLeader>> response) {
                mProgressBar.setVisibility(View.INVISIBLE);
                ArrayList<SkillIqLeader> learningLeaders = response.body();
                mLeadersAdapter.changeData(learningLeaders);
            }

            @Override
            public void onFailure(Call<ArrayList<SkillIqLeader>> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}