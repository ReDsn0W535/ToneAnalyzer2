package com.example.myapplication.NetClient;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Api {

    @GET("tone")
    Call<JsonObject> getToneAlanyze(@Query("version") String version, @Query("text") String text);

}
