package com.example.myapplication.NetClient;

import com.example.myapplication.NetClient.Api;
import com.example.myapplication.NetClient.BasicAuthInterceptor;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private Retrofit retrofit;
    private Api api;
    private static Client ourInstance = new Client();
    private static final String BASE_URL = "https://gateway-wdc.watsonplatform.net/tone-analyzer/api/v3/";
    public static Client getInstance() {
        return ourInstance;
    }
    private String username;
    private String password;
    private Client() {
    }
    public void createClient(){
        ArrayList<Protocol> protocols = new ArrayList<>();
        protocols.add(Protocol.HTTP_1_1);
        OkHttpClient client = new OkHttpClient.Builder()
                .protocols(protocols)
                .authenticator((route, response) -> {
                    Request request = response.request();
                    if (request.header("Authorization") != null)
                        return null;
                    return request.newBuilder()
                            .header("Authorization", Credentials.basic(username, password))
                            .build();
                })
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Api createApi(){
        return retrofit.create(Api.class);
    }
}
