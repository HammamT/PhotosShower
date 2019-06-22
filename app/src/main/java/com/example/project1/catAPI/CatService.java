package com.example.project1.catAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CatService {
    @GET("/v1/images/search")
    Call<List<CatResponse>> get(@Query("format") String format);
}
