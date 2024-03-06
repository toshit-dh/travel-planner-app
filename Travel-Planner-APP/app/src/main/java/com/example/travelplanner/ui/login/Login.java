package com.example.travelplanner.ui.login;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelplanner.R;
import com.example.travelplanner.activities.Home;
import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.api.SignUpResponse;
import com.example.travelplanner.data.LoginUser;
import com.example.travelplanner.data.MyPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;
    TextView forgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginUsername = findViewById(R.id.login_username);
        forgotPassword = findViewById(R.id.forgot_password);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signUpRedirectText);

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = loginUsername.getText().toString();
                String password = loginPassword.getText().toString();
                loginApi(username,password);
            }
        });
    }
    private void loginApi(String username,String password){
        LoginUser loginUser = new LoginUser(username, password);
        RetrofitInstance.getInstance().apiInterface.onLogin(loginUser).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if(response.isSuccessful()){
                    SignUpResponse signUpResponse = response.body();
                    MyPrefs.setToken(Login.this, signUpResponse.getToken());
                    Toast.makeText(Login.this, "You have logged in successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else{
                    if(response.code() == 403){
                        Toast.makeText(Login.this,"Verify Email First",Toast.LENGTH_SHORT).show();
                    }
                    handleValidationErrors(response);
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.e("errorsignup", t.getMessage());
            }
        });
    }
    private void handleValidationErrors(Response<SignUpResponse> response) {
        if (response.errorBody() != null) {
            try {
                String errorBodyString = response.errorBody().string();
                JSONObject errorObject = new JSONObject(errorBodyString);
                String errorMsg = errorObject.optString("msg", "Unknown error");
                boolean status = errorObject.optBoolean("status", false);
                if (!status) {
                    Toast.makeText(Login.this,errorMsg,Toast.LENGTH_SHORT).show();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
