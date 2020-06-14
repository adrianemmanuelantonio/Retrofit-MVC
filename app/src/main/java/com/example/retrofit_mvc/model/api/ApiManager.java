package com.example.retrofit_mvc.model.api;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiManager {
    private static final String BASE_URL = "http://phisix-api.appspot.com";
    private StocksApi stocksApi;

    public StocksApi getStocksApi() {
        if (stocksApi == null) {
            stocksApi = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
                    .create(StocksApi.class);
        }
        return stocksApi;
    }
}
