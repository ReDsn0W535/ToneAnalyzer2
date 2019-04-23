package com.example.myapplication.NetClient;

import java.util.ArrayList;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwitterClient {
    private Retrofit retrofit;
    private Api api;
    private static TwitterClient ourInstance = new TwitterClient();
    private static final String BASE_URL = "https://twitter.com/";
    public static TwitterClient getInstance() {
        return ourInstance;
    }
    private String username;
    private String password;
    private TwitterClient() {
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
