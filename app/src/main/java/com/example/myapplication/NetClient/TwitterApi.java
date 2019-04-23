package com.example.myapplication.NetClient;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TwitterApi {
    @GET("{name}/status/{id}")
    Call<JsonObject> getToneAlanyze(@Path ("name") String name, @Path("id") String id);
}
