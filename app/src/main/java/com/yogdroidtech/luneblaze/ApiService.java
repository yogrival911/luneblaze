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

    @Headers({"Authorization: key=AAAA5fI4UdY:APA91bFX9doqYgeHwG4Fh41qd7w_REBIUDXIethEdErkTN0YcLfuUzhoFmw3hyd5LDPblMWptAi48ZLl56UZlf4M725wJj32hmRaAJ2BoEIjqDRUPe_jLumQWi9IQNQLDtZhDL9fOi-3",
            "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body JsonObject jsonObject);
}
