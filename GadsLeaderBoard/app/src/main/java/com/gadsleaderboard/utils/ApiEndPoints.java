package com.gadsleaderboard.utils;

import com.gadsleaderboard.models.LearningLeader;
import com.gadsleaderboard.models.SkillIqLeader;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiEndPoints {
    @GET("/api/hours")
    Call<ArrayList<LearningLeader>> getLearningLeaders();

    @GET("/api/skilliq")
    Call<ArrayList<SkillIqLeader>> getSkillLeaders();

    @POST
    @FormUrlEncoded
    Call<Void> submitProject(
            @Url String url,
            @Field("entry.1824927963") String email,
            @Field("entry.1877115667") String name,
            @Field("entry.2006916086") String lastName,
            @Field("entry.284483984") String projectLink
    );
}
