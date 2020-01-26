package com.anlisoft.vsafe.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.anlisoft.vsafe.models.data.Global.gRetrofit;

public class RetrofitClient {

    public RetrofitClient() {
    }

    public static Retrofit getClient(String baseUrl) {

        if (gRetrofit == null) {
            gRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return gRetrofit;
    }
}


