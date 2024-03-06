package com.example.travelplanner.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travelplanner.R;
import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.data.MyPrefs;
import com.example.travelplanner.data.UserData;
import com.example.travelplanner.ui.login.Login;
import com.example.travelplanner.ui.login.Signup;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Start extends AppCompatActivity {
    private ImageView animatedImage;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        animatedImage = findViewById(R.id.animatedImage);
        Glide.with(this).asGif().load(R.drawable.start).into(animatedImage);
        MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.appstartsound);
        mediaPlayer.start();
        textView = findViewById(R.id.plantext);
        textView.setSelected(true);
        Looper mainLooper = Looper.getMainLooper();
        new Handler(mainLooper).postDelayed(() -> {
            if(MyPrefs.getToken(Start.this).equals("")){
                if(MyPrefs.getIntroCompletedStatus(Start.this)){
                    Toast.makeText(Start.this,"login",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Start.this,Login.class));
                }
                else {
                    Log.e("explore","explore");
                    startActivity(new Intent(Start.this,Login.class));
                }
            }
            else{
                getDataApi();
            }

        }, 2000);
    }
    private void getDataApi(){
        Map<String,String> userHeaders = new HashMap<>();
        userHeaders.put("Content-Type", "application/json");
        userHeaders.put("Authorization", MyPrefs.getToken(Start.this));
        RetrofitInstance.getInstance().apiInterface.onGetData(userHeaders).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if(response.isSuccessful()){
                    UserData userData = response.body();
                    Gson gson = new Gson();
                    String data = gson.toJson(userData);
                    Log.e("data",data);
                    MyPrefs.setData(Start.this,data);
                    startActivity(new Intent(Start.this,Home.class));
                }
                else{
                    Log.e("errorget",response.errorBody().toString());
                    startActivity(new Intent(Start.this,Login.class));
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.e("getdata",t.getMessage());
                startActivity(new Intent(Start.this,Login.class));
            }
        });
    }
}
