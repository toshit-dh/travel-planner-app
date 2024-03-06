package com.example.travelplanner.fragments;



import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.travelplanner.R;
import com.example.travelplanner.adapters.MyTripsAdapter;
import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.data.MyPrefs;
import com.example.travelplanner.data.Trip;
import com.example.travelplanner.data.Trips;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TripFragment extends Fragment {
    private List<Trips> tripsList = new ArrayList<>();
    private ProgressBar progressBar;
    private MyTripsAdapter tripAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.tripsRecyclerView);
        progressBar = view.findViewById(R.id.progressBartrips);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        Map<String, String> userHeaders = new HashMap<>();
        userHeaders.put("Content-Type", "application/json");
        userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));
        RetrofitInstance.getInstance().apiInterface.onTripData(userHeaders).enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                if(response.isSuccessful()){
                    List<Trip> trips = response.body();
                    tripAdapter = new MyTripsAdapter(trips);
                    recyclerView.setAdapter(tripAdapter);
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    Log.e("error",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                Log.e("servererror",t.getMessage());
            }
        });
        return view;
    }
}
