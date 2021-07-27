package com.yogdroidtech.luneblaze;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    private ImageView imageView;
    private Button selectBtn, proceed;
    private EditText etArtName,etUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        selectBtn = findViewById(R.id.selectBtn);
        proceed = findViewById(R.id.proceed);
        etArtName = findViewById(R.id.etArtName);
        etUserName = findViewById(R.id.etUserName);

        selectBtn.setOnClickListener(this::onClick);
        proceed.setOnClickListener(this::onClick);
//        getToken();

    }

    private void selectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.selectBtn:
                selectImage();
                break;
            case R.id.proceed:

                if(isInputValid()){
                    Intent intent = new Intent(this,SecondActivity.class);
                    intent.putExtra("imageUri", filePath.toString());
                    startActivity(intent);
                }
                break;
        }
    }

    private Boolean isInputValid() {
        String artName = etArtName.getText().toString();
        String userName = etUserName.getText().toString();
        if(artName.trim().isEmpty()){
            etArtName.setError("Please provide article name");
            return false;
        }
        else if(userName.trim().trim().isEmpty()){
            etUserName.setError("Please provide username");
            return false;
        }
        else if(filePath==null){
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }

    }

}