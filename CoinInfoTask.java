package com.example.android.rbtreecrypto.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.rbtreecrypto.BuildCoinInfo;
import com.example.android.rbtreecrypto.DBHelper;
import com.example.android.rbtreecrypto.ModelContainer;
import com.example.android.rbtreecrypto.Helper;


import static com.example.android.rbtreecrypto.FragmentInfo.COIN_TYPE_INFO;
import static com.example.android.rbtreecrypto.FragmentInfo.COIN_TYPE_MOREINFO;
import static com.example.android.rbtreecrypto.Helper.JSON_SUCCESS_MESSAGE;


public class CoinInfoTask extends AsyncTask<String, Integer, ModelContainer> {

    private static final String API_URL_PRICEMULTIFULL = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms=";
    private static final String API_PRICEMULTIFULL_TSYMS = "&tsyms=BTC,ETH,EVN,DOGE,ZEC,USD,EUR";
    private static final String API_URL_GENERALINFO = "https://min-api.cryptocompare.com/data/coin/generalinfo?fsyms=";
    private static final String TSYM_USD = "&tsym=USD";

    private Context mContext;
    StringBuilder HttpResponse;
    private String ErrorResponse;

    public CoinInfoTask(Context context) {
        mContext = context;
    }

    @Override
    protected ModelContainer doInBackground(String... params) {
        final String CoinPick = params[0];
        final String Type = params[1];
        String url;
        DBHelper DBCache;
        DBCache = new DBHelper(mContext);
        ModelContainer modelContainer = new ModelContainer();
        switch (Type) {
            case COIN_TYPE_INFO: {
                url = API_URL_PRICEMULTIFULL + CoinPick + API_PRICEMULTIFULL_TSYMS;
                HttpResponse = Helper.getHtppResponse(url);
                ErrorResponse = Helper.errorParser(HttpResponse);
                modelContainer.ResponseError = ErrorResponse;
                Log.d("##", ErrorResponse);
                if (ErrorResponse.equals(JSON_SUCCESS_MESSAGE)) {

                    if (DBCache.get("COIN_INFO","COIN","COIN",CoinPick)==null || DBCache.get("COIN_INFO","COIN","COIN",CoinPick).isEmpty()) {
                        Log.d("OUT JE#", "  PRAZAN");
                        DBCache.insertData("COIN_INFO", CoinPick, HttpResponse.toString().replace("\'", "&&"),"COIN","VALUE");
                    }
                    else {
                        Log.d("OUT JE#", "  PUN!");
                        DBCache.update("COIN_INFO",HttpResponse.toString().replace("\'", "&&"), "VALUE","COIN",CoinPick);

                    }
                }
                HttpResponse = new StringBuilder(DBCache.get("COIN_INFO","VALUE","COIN",CoinPick).replace("&&","\'"));
                if (HttpResponse.toString()!=null && !HttpResponse.toString().isEmpty()) {
                    Log.d("##", "BAZA");
                    modelContainer.CoinInfoList = Helper.getCoinInfo(HttpResponse, CoinPick);
                }
                break;
            }

                case COIN_TYPE_MOREINFO: {
                    url = API_URL_GENERALINFO + CoinPick + TSYM_USD;
                    HttpResponse = Helper.getHtppResponse(url);
                    ErrorResponse = Helper.errorParser(HttpResponse);
                    modelContainer.ResponseError = ErrorResponse;
                    if (ErrorResponse.equals(JSON_SUCCESS_MESSAGE)) {
                        modelContainer.MoreInfoCoin = Helper.getMoreCoinInfo(HttpResponse);
                        modelContainer.ConversionCoin = Helper.getConversionInfoCoin(HttpResponse);
                    }
                    break;
                }
                }
                return modelContainer;
        }


    @Override
    protected void onPostExecute(ModelContainer result) {

        super.onPostExecute(result);
        BuildCoinInfo.createInfo(mContext, result);
    }

}