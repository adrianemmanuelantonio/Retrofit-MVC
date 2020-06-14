package com.example.retrofit_mvc.view;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.retrofit_mvc.R;
import com.example.retrofit_mvc.controller.StocksController;
import com.example.retrofit_mvc.model.pojo.StockPojo;

import java.util.Locale;

/**
 * Simple project about searching stocks
 * Uses MVC design pattern
 * Implements Retrofit2 and Gson
 *
 * Pojo classes can be generated using the website:
 * http://www.jsonschema2pojo.org/
 *
 * Stock data source:
 * http://phisix-api.appspot.com
 *
 * @author  Adrian Emmanuel Antonio
 * @version 1.0
 * @since   2020-06-14
 */
public class MainActivity extends AppCompatActivity implements StocksController.StocksResponse {
    private static final String STOCK_ERROR = "Please type a stock symbol";
    private static final String SEARCHING_TEXT = "Searching. Please wait.";
    private static final String FORMAT = "%.2f";
    private static final int ZERO = 0;

    private AppCompatEditText searchTxt;
    private AppCompatButton searchBtn;
    private AppCompatTextView stockNameTxt;
    private AppCompatTextView stockSymbolTxt;
    private AppCompatTextView stockPriceTxt;

    private StocksController stocksController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupListeners();
        initializeController();
    }

    private void setupViews() {
        searchTxt = findViewById(R.id.stockTxt);
        searchBtn = findViewById(R.id.search_btn);
        stockNameTxt = findViewById(R.id.stock_name_txt);
        stockSymbolTxt = findViewById(R.id.stock_symbol_txt);
        stockPriceTxt = findViewById(R.id.stock_price_txt);
    }

    private void setupListeners() {
        searchBtn.setOnClickListener(v -> searchStocks());
        searchTxt.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                searchStocks();
            }
            return false;
        });
    }

    private void initializeController() {
        stocksController = new StocksController();
    }

    private void searchStocks() {
        if (searchTxt.getText() == null || searchTxt.getText().toString().trim().length() == ZERO) {
            Toast.makeText(this, STOCK_ERROR, Toast.LENGTH_LONG).show();
            return;
        }

        disableViews();
        hideKeyboard(searchTxt);
        stocksController.searchStock(searchTxt.getText().toString().trim(), this);
    }

    private void showStockDetails(StockPojo stockPojo) {
        // This particular api endpoint returns one stock on the list
        for (StockPojo.Stock stock : stockPojo.getStock()) {
            stockNameTxt.setText(stock.getName());
            stockSymbolTxt.setText(stock.getSymbol());
            stockPriceTxt.setText(String.format(Locale.ENGLISH, FORMAT, stock.getPrice().getAmount()));
        }
    }

    private void setButtonText(String text) {
        searchBtn.setText(text);
    }

    private void enableViews() {
        setButtonText(this.getString(R.string.search_button_text));
        searchTxt.setEnabled(true);
        searchBtn.setEnabled(true);
    }

    private void disableViews() {
        setButtonText(SEARCHING_TEXT);
        searchTxt.setEnabled(false);
        searchBtn.setEnabled(false);
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), ZERO);
        }
    }

    @Override
    public void onStocksSuccess(StockPojo stockPojo) {
        showStockDetails(stockPojo);
        enableViews();
    }

    @Override
    public void onStocksFailed() {
        Toast.makeText(this, "Failed to get stock data", Toast.LENGTH_LONG).show();
        enableViews();
    }
}
