package com.example.travelplanner.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelplanner.R;
import com.example.travelplanner.adapters.ItenaryAdapter;
import com.example.travelplanner.api.ItenaryData;
import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.data.MyPrefs;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItenaryFragment extends Fragment {
    private TextView location;
    private TextView stay_days;
    private TextView budget;
    private ImageView getData;
    private ProgressBar progressBar;
    private RecyclerView itenaryDataRecycler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itenary,container,false);
        location = view.findViewById(R.id.location);
        stay_days = view.findViewById(R.id.staydays);
        budget = view.findViewById(R.id.yourbudget);
        getData = view.findViewById(R.id.searchdata);
        progressBar = view.findViewById(R.id.loaditenary);
        itenaryDataRecycler = view.findViewById(R.id.itenarydata);
        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(location.getText().toString()!=null && stay_days.getText().toString()!=null && budget.getText().toString()!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    Map<String, String> userHeaders = new HashMap<>();
                    userHeaders.put("Content-Type", "application/json");
                    userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));
                    RetrofitInstance.getInstance().apiInterface.onItenaryData(location.getText().toString(),stay_days.getText().toString(),budget.getText().toString(),userHeaders).enqueue(new Callback<ItenaryData>() {
                        @Override
                        public void onResponse(Call<ItenaryData> call, Response<ItenaryData> response) {
                            if(response.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                ItenaryData itenaryData = response.body();
                                ItenaryAdapter itenaryAdapter = new ItenaryAdapter(itenaryData.getDayPlan(),itenaryData.getIntrestingActivities());
                                itenaryDataRecycler.setAdapter(itenaryAdapter);
                                itenaryDataRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
                                Toast.makeText(requireContext(),itenaryData.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(requireContext(),"Can't get data.",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ItenaryData> call, Throwable t) {
                            Log.e("servererror",t.getMessage());
                        }
                    });
                }else{
                    Toast.makeText(requireContext(),"Fill al fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}