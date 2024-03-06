package com.example.travelplanner.ui.login;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.IOException;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.travelplanner.R;
import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.api.SignUpResponse;
import com.example.travelplanner.data.MyPrefs;
import com.example.travelplanner.data.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class Signup extends AppCompatActivity {
    EditText signupName, signupUsername, signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();
                if(validateInput(name,email,username,password)){
                    signUpApi(name, email, username, password);
                }
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });
    }
    private boolean validateInput(String name, String email,String username, String password) {
        boolean isValid = true;
        if (name.length() <= 5) {
            isValid = false;
            signupName.setError("Name must be longer than 5 characters");
        }
        if (email.length() <= 5) {
            isValid = false;
            signupEmail.setError("Email must be longer than 5 characters");
        }
        if (username.length() <= 5 || Character.isUpperCase(username.charAt(0))){
            isValid = false;
            signupUsername.setError("Username must be longer than 5 characters and should start with lowercase");
        }
        if (password.length() <= 10) {
            isValid = false;
            signupPassword.setError("Password must be longer than 10 characters");
        }
        return isValid;
    }
    private void signUpApi(String name, String email, String username, String password) {
        User user = new User(name, username, email, password);
        RetrofitInstance.getInstance().apiInterface.onSignup(user).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.isSuccessful()) {
                    SignUpResponse signUpResponse = response.body();
                    MyPrefs.setToken(Signup.this, signUpResponse.getToken());
                    Toast.makeText(Signup.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Signup.this, Login.class);
                    startActivity(intent);
                }
                else{
                    Log.e("error","iugc");
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
                    Toast.makeText(Signup.this, errorMsg, Toast.LENGTH_SHORT).show();
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
