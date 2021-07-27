package com.yogdroidtech.luneblaze;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout informBtn;
    private String token;
    private ImageView ivInform, userImg;
    private TextView tvInform;
    private Boolean isClicked = true;
    private String notiText;
    private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        String fileString  = getIntent().getStringExtra("imageUri");
        filePath = Uri.parse(fileString);



        informBtn = findViewById(R.id.informBtn);
        ivInform = findViewById(R.id.imageView4);
        tvInform = findViewById(R.id.textView5);
        userImg = findViewById(R.id.imageView2);
        informBtn.setOnClickListener(this::onClick);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
//        getToken();

        setImg();
    }

    private void setImg() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore
                    .Images
                    .Media
                    .getBitmap(
                            getContentResolver(),
                            filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        userImg.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.informBtn:
                callApi();
                break;
        }
    }

    private void callApi() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id","94");
        jsonObject.addProperty("type","article");
        jsonObject.addProperty("content_id","142");
        jsonObject.addProperty("reaction","0");

        ApiService apiService = ApiClient.getClient(AppConstants.BASE_URL).create(ApiService.class);

        Call<ApiResponse> call = apiService.getApiResponse(jsonObject);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful()){
                    Log.i("Yog", response.body().toString());
//                   FCMCall();
                    if(isClicked){
                        ivInform.setBackgroundResource(R.drawable.ic_baseline_menu_book_24_blue);
                        tvInform.setTextColor(Color.parseColor("#4500FD"));
                        isClicked = false;
                        notiText = "You Clicked Informataive";
                    }
                    else {
                        ivInform.setBackgroundResource(R.drawable.ic_baseline_menu_book_24);
                        tvInform.setTextColor(Color.BLACK);
                        isClicked = true;
                        notiText = "You Unclicked Informataive";
                    }
                    getToken();
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("Yog", t.toString());

            }
        });


    }

    private void FCMCall() {
        FirebaseMessaging.getInstance().subscribeToTopic("all")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribed failed";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(SecondActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void getToken()
    { FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();
                        sendNotification();


                        // Log and toast
                        String msg = token;
                        Log.d("TAG", msg);
                        Toast.makeText(SecondActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void sendNotification() {
        JsonObject jsonObject = buildNotificationPayload();
        ApiService apiService = FireClient.getClient(AppConstants.FIRE_BASE_URL).create(ApiService.class);

        Call<ResponseBody> call = apiService.sendNotification(jsonObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("yogesh", response.body().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("yogesh", t.toString());

            }
        });
    }

    private JsonObject buildNotificationPayload() {
        // compose notification json payload
        JsonObject payload = new JsonObject();
        payload.addProperty("to", token);

        // compose data payload here
        JsonObject data = new JsonObject();
        JsonObject noti = new JsonObject();
        JsonObject notiData = new JsonObject();
        notiData.addProperty("body", notiText);
        notiData.addProperty("title", "Informative");
        payload.add("notification",notiData );
        data.addProperty("title", "New Notification");
        data.addProperty("message", "You clicked informative button");
//        data.addProperty(key.getText().toString(), value.getText().toString());
        // add data payload
        payload.add("data", data);
        return payload;
    }
}