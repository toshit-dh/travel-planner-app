package com.example.travelplanner.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.travelplanner.R;
import com.example.travelplanner.adapters.NotificationAdapter;
import com.example.travelplanner.adapters.UsersAdapter;
import com.example.travelplanner.api.ChatItems;
import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.data.MyPrefs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifFragment extends Fragment {
    private RecyclerView notifRecycler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notif,container,false);
        notifRecycler = view.findViewById(R.id.notifrecycler);
        Map<String, String> userHeaders = new HashMap<>();
        userHeaders.put("Content-Type", "application/json");
        userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));
        RetrofitInstance.getInstance().apiInterface.onChatItems(userHeaders).enqueue(new Callback<ChatItems>() {
            @Override
            public void onResponse(Call<ChatItems> call, Response<ChatItems> response) {
                if (response.isSuccessful()) {
                    ChatItems chatItems = response.body();
                    List<ChatItems.User> friendsRequestList = chatItems.getFriendsRequests();
                    List<ChatItems.Trip> tripsRequest = chatItems.getTripsRequests();
                    NotificationAdapter notificationAdapter = new NotificationAdapter(friendsRequestList,tripsRequest);
                    notifRecycler.setAdapter(notificationAdapter);
                    notifRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));

                } else {
                    Log.e("error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ChatItems> call, Throwable t) {
                Log.e("servererror", t.getMessage());
            }
        });
        return view;
    }
}