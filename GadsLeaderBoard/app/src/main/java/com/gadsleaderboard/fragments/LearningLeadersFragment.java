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
import com.gadsleaderboard.models.LearningLeader;
import com.gadsleaderboard.adapters.LearningLeadersAdapter;
import com.gadsleaderboard.R;
import com.gadsleaderboard.utils.RetrofitBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LearningLeadersFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LearningLeadersAdapter mLeadersAdapter;
    private ProgressBar mProgressBar;

    public LearningLeadersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_learning_leaders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler_learning_leaders);
        mLeadersAdapter = new LearningLeadersAdapter();
        mRecyclerView.setAdapter(mLeadersAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mProgressBar = view.findViewById(R.id.progressBar);

        mProgressBar.setVisibility(View.VISIBLE);
        ApiEndPoints service = RetrofitBuilder.buildSerVice(ApiEndPoints.class);
        Call<ArrayList<LearningLeader>> call = service.getLearningLeaders();

        call.enqueue(new Callback<ArrayList<LearningLeader>>() {
            @Override
            public void onResponse(Call<ArrayList<LearningLeader>> call, Response<ArrayList<LearningLeader>> response) {
                mProgressBar.setVisibility(View.INVISIBLE);
                ArrayList<LearningLeader> learningLeaders = response.body();
                mLeadersAdapter.changeData(learningLeaders);
            }

            @Override
            public void onFailure(Call<ArrayList<LearningLeader>> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}