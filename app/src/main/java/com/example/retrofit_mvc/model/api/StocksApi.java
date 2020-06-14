package com.example.retrofit_mvc.model.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface StocksApi {
    @GET()
    Call<String> get(@Url String apiUrl);
}
