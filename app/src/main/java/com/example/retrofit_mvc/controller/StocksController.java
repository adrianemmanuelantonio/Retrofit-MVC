package com.example.retrofit_mvc.controller;

import android.util.Log;

import com.example.retrofit_mvc.BuildConfig;
import com.example.retrofit_mvc.model.api.ApiManager;
import com.example.retrofit_mvc.model.pojo.StockPojo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * StocksController
 * Controller to request stock data from http://phisix-api.appspot.com
 * Uses Retrofit2 and Gson
 *
 * @author  Adrian Emmanuel Antonio
 * @version 1.0
 * @since   2020-06-10
 */

public class StocksController {
    private static final int STATUS_OKAY = 200;
    private static final String TAG = "StocksController";
    private static final String STOCKS_ENDPOINT = "http://phisix-api.appspot.com/stocks/{stock_symbol}.json";
    private static final String STOCK_SYMBOL = "{stock_symbol}";

    private ApiManager apiManager;
    private Gson gson;

    /**
     * This is the constructor of the controller
     * Initializes the apiManager and gson
     */
    public StocksController() {
        this.apiManager = new ApiManager();
        this.gson = new GsonBuilder().create();
    }

    /**
     * Method to search the stock details
     * Uses a unique interface to handle the specific request
     *
     * @param stockSymbol This is the stock symbol to be searched on the api
     * @param stocksResponse This is the interface to handle the response
     */
    public void searchStock(String stockSymbol, StocksResponse stocksResponse) {
        String url = STOCKS_ENDPOINT.replace(STOCK_SYMBOL, stockSymbol);
        Call<String> call = apiManager.getStocksApi().get(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                try {
                    if (response.code() == STATUS_OKAY) {
                        StockPojo result = gson.fromJson(response.body(), StockPojo.class);
                        if (result != null) {
                            stocksResponse.onStocksSuccess(result);
                        } else {
                            stocksResponse.onStocksFailed();
                        }
                    } else {
                        stocksResponse.onStocksFailed();
                    }
                } catch (Exception ex) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, ex.toString());
                    }
                    stocksResponse.onStocksFailed();
                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                stocksResponse.onStocksFailed();
            }
        });
    }

    /**
     * Interface to handle the searching of specific stock
     */
    public interface StocksResponse {
        void onStocksSuccess(StockPojo stockPojo);
        void onStocksFailed();
    }
}
