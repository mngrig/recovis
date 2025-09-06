package com.example.recovis.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class RetrofitService {

    private Retrofit retrofit;

    public RetrofitService(){
        initializeRetrofit();
    }

    private void initializeRetrofit(){
        // Create an OkHttpClient with logging interceptor
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        // Create a logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Add the logging interceptor to the OkHttpClient
        httpClientBuilder.addInterceptor(loggingInterceptor);

        // Build the OkHttpClient
        OkHttpClient httpClient = httpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
