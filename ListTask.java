package com.example.android.rbtreecrypto.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.rbtreecrypto.DBHelper;
import com.example.android.rbtreecrypto.ModelContainer;
import com.example.android.rbtreecrypto.Helper;
import com.example.android.rbtreecrypto.ListTaskResult;
import com.example.android.rbtreecrypto.models.SingleCoinModel;

import java.util.List;

import static com.example.android.rbtreecrypto.Helper.JSON_SUCCESS_MESSAGE;

public class ListTask extends AsyncTask<String, Integer, ModelContainer> {
    private static final String ALL_COINS_URL = "https://min-api.cryptocompare.com/data/all/coinlist";

    public ListTaskResult delegate = null;

    StringBuilder HttpResponse;
    String ErrorResponse;
    private Context mContext;
    String currentDBPath = "/data/data/com.example.android.rbtreecrypto/databases/DB_CACHE.db";

    public ListTask(Context context) {
        mContext = context;
    }

    @Override
    protected ModelContainer doInBackground(String... params) {

        HttpResponse = Helper.getHtppResponse(ALL_COINS_URL);
        ErrorResponse = Helper.errorParser(HttpResponse);

        DBHelper DBCache;
        DBCache = new DBHelper(mContext);

        ModelContainer CoinModel = new ModelContainer();
        CoinModel.ResponseError = ErrorResponse;


        if (ErrorResponse.equals(JSON_SUCCESS_MESSAGE)) {

            //if (DBCache.isFieldExist("COIN_LIST"))
            if (DBCache.get("JSON_DATA","COIN_LIST","id","1")==null || DBCache.get("JSON_DATA","COIN_LIST","id","1").isEmpty()) {
              //  Log.d("OUT JE#", "  PRAZAN");
                DBCache.insertData("JSON_DATA",HttpResponse.toString().replace("\'", "&&"),null ,"COIN_LIST", null);
            }
            else {
            //   Log.d("OUT JE#", "  PUN!");
                DBCache.update("JSON_DATA",HttpResponse.toString().replace("\'", "&&"), "COIN_LIST","id","1");

            }
            // else
//                DBCache.insertData(HttpResponse.toString(), "COIN_LIST");
        }
     //   Log.d("OUT JE#", "  IZ BAZE");
        HttpResponse = new StringBuilder(DBCache.get("JSON_DATA","COIN_LIST","id","1").replace("&&","\'"));

        // CoinModel.AllTheCoins = Helper.getAllCoins(DBCache.get("COIN_LIST"));
        if (HttpResponse.toString()!=null && !HttpResponse.toString().isEmpty())
            CoinModel.AllTheCoins = Helper.getAllCoins(HttpResponse);

        //    CoinModel.AllTheCoins = Helper.getAllCoins(DBCache.get("COIN_LIST"));
        return CoinModel;

    }
    @Override
    protected void onPostExecute(ModelContainer result) {

        delegate.processFinish(result);

    }


}



