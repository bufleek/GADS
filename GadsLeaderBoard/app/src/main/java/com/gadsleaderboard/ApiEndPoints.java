package com.gadsleaderboard;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiEndPoints {
    @GET("/api/hours")
    Call<ArrayList<LearningLeader>> getLearningLeaders();

    @GET("/api/skilliq")
    Call<ArrayList<SkillIqLeader>> getSkillLeaders();
}
