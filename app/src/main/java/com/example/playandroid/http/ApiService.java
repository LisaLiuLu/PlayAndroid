package com.example.playandroid.http;

import com.example.playandroid.model.MainPageBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {

    @GET
    Call<MainPageBean> getData(@Url String url);

    @GET("article/list/{path}/json")
    Call<MainPageBean> getMainPage(@Path("path") int path);


}
