package com.example.travelplanner.fragments;



import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.travelplanner.R;
import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.data.MyPrefs;
import com.example.travelplanner.data.UserData;
import com.example.travelplanner.ui.login.Signup;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {
    TextView profileName, profileEmail, profileUsername, profilePassword;
    TextView titleName, titleUsername;
    Button editProfile,logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        profileName =view.findViewById(R.id.profileName);
        profileEmail =view.findViewById(R.id.profileEmail);
        profileUsername =view.findViewById(R.id.profileUsername);
        logout = view.findViewById(R.id.btnLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPrefs.removeToken(requireContext());
                startActivity(new Intent(getActivity(), Signup.class));
            }
        });
        getDataApi();
        return view;
    }
    private void getDataApi(){
        Map<String,String> userHeaders = new HashMap<>();
        userHeaders.put("Content-Type", "application/json");
        userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));
        RetrofitInstance.getInstance().apiInterface.onGetData(userHeaders).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if(response.isSuccessful()){
                    UserData userData = response.body();
                    profileName.setText(userData.getName());
                    profileEmail.setText(userData.getEmail());
                    profileUsername.setText(userData.getUsername());
                }
                else{
                    Log.e("errorget","cant get");
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.e("getdata",t.getMessage());
            }
        });
    }
}