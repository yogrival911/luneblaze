package com.yogdroidtech.luneblaze;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @POST("react.json")
    Call<ApiResponse> getApiResponse(@Body JsonObject jsonObject);

    @Headers({"Authorization: key=AAAA5fI4UdY:-3",
            "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body JsonObject jsonObject);
}
