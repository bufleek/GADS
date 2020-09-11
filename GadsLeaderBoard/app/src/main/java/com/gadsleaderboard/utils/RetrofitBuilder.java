package com.gadsleaderboard.utils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    private static final String BASE_URL = "https://gadsapi.herokuapp.com/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <S> S buildSerVice(Class<S> sClass){
        return retrofit.create(sClass);
    }
}
