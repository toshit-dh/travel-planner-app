package com.example.travelplanner.fragments;
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
import com.example.travelplanner.adapters.DestinationAdapter;
import com.example.travelplanner.api.DestinationData;
import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.data.MyPrefs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        recyclerView = view.findViewById(R.id.destinationrecycler);
        progressBar = view.findViewById(R.id.progressBardest);
        Map<String, String> userHeaders = new HashMap<>();
        userHeaders.put("Content-Type", "application/json");
        userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));
        RetrofitInstance.getInstance().apiInterface.onDestData("hello",userHeaders).enqueue(new Callback<List<DestinationData>>() {
            @Override
            public void onResponse(Call<List<DestinationData>> call, Response<List<DestinationData>> response) {
                if(isAdded()){
                if(response.isSuccessful()){
                    List<DestinationData> destinationData = response.body();
                    progressBar.setVisibility(View.GONE);
                    DestinationAdapter destinationAdapter = new DestinationAdapter(destinationData);
                    recyclerView.setAdapter(destinationAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false));
                }}
                else{
                    Log.e("error","i");
                }
            }

            @Override
            public void onFailure(Call<List<DestinationData>> call, Throwable t) {
                Log.e("servererror",t.getMessage());
            }
        });
        return view;
    }
}