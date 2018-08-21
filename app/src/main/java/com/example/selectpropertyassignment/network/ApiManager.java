package com.example.selectpropertyassignment.network;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class ApiManager {
    private static final ApiManager instance = new ApiManager();

    private final String baseUrl = "https://mybataz.com/";

    public static ApiManager getInstance(){
        return instance;
    }

    public  String url = "https://my-json-server.typicode.com/iranjith4/ad-assignment/db";

    private <T> T createRetrofitService(Class<T> service){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(MyLoggingInterceptor.provideOkHttpLogging());

        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(service);
    }

    private WikiApiClient getService(){
        return createRetrofitService(WikiApiClient.class);
    }

    private interface WikiApiClient{
        @GET
        Call<ResponseBody> search(@Url String url);

    }

    public Call<ResponseBody> searchApi(String url){
        return getService().search(url);
    }

}
