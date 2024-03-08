package com.example.travelplanner.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplanner.R;
import com.example.travelplanner.activities.OnItemClickListener;

import com.example.travelplanner.adapters.UsersAdapter;
import com.example.travelplanner.api.ChatItems;
import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.data.MyPrefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {
    private LinearLayout users;
    private LinearLayout friensTRips;
    private RecyclerView usersRecycler;
    private RecyclerView friendsRecycer;
    private RecyclerView tripsRecycler;
    private EditText searchUsers;
    private List<ChatItems.User> userList ;
    private List<ChatItems.User> friendsList ;
    private List<ChatItems.Trip> tripList ;
    private UsersAdapter usersAdapter;
    private OnItemClickListener onItemClickListener;
    public ChatFragment(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        usersRecycler = view.findViewById(R.id.usersrecycler);
        friendsRecycer = view.findViewById(R.id.friendrecycler);
        tripsRecycler = view.findViewById(R.id.otriprecycler);
        users = view.findViewById(R.id.searchuserlinear);
        friensTRips = view.findViewById(R.id.searchfriendslinear);
        searchUsers = view.findViewById(R.id.searchuserstext);
        Map<String, String> userHeaders = new HashMap<>();
        userHeaders.put("Content-Type", "application/json");
        userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));
        RetrofitInstance.getInstance().apiInterface.onChatItems(userHeaders).enqueue(new Callback<ChatItems>() {
            @Override
            public void onResponse(Call<ChatItems> call, Response<ChatItems> response) {
                if (response.isSuccessful()) {
                    ChatItems chatItems = response.body();
                    userList = chatItems.getUsers();
                    List<ChatItems.User> friendsList = chatItems.getFriends();
                    List<ChatItems.Trip> tripList = chatItems.getTrips();
                    usersAdapter = new UsersAdapter(userList, "Users");
                    usersRecycler.setAdapter(usersAdapter);
                    usersRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
                    UsersAdapter friendRequestAdapter = new UsersAdapter(friendsList, "Friends");
                    friendsRecycer.setAdapter(friendRequestAdapter);
                    friendsRecycer.setLayoutManager(new LinearLayoutManager(requireContext()));
                    TripsAdapter tripsAdapter = new TripsAdapter(tripList,onItemClickListener);
                    tripsRecycler.setAdapter(tripsAdapter);
                    tripsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
                } else {
                    Log.e("error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ChatItems> call, Throwable t) {
                Log.e("servererror", t.getMessage());
            }
        });
        searchUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchUsers.setVisibility(View.GONE);
                friensTRips.setVisibility(View.GONE);
                users.setVisibility(View.VISIBLE);
            }
        });
        searchUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                List<ChatItems.User> filteredUsers = new ArrayList<>();
                Log.e("ii","user"+userList.get(0).get_id());
                for (ChatItems.User user : userList) {
                    if (user.getUsername().toLowerCase().contains(searchUsers.getText().toString().toLowerCase())) {
                        filteredUsers.add(user);
                        usersAdapter.filterList(filteredUsers);
                    }
                }
            }
        });
        return view;
    }

    public static class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {
        private List<ChatItems.Trip> trips;
        private OnItemClickListener onItemClickListener;

        public TripsAdapter(List<ChatItems.Trip> trips, OnItemClickListener listener) {
            this.trips = trips;
            this.onItemClickListener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_trip, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ChatItems.Trip trip = trips.get(position);
            holder.id.setText(trip.get_id());
            holder.city.setText(trip.getCity());
            holder.creator.setText(trip.getCreator());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(trip);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return trips.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private TextView id;
            private TextView city;
            private TextView creator;

            ViewHolder(View itemView) {
                super(itemView);
                id = itemView.findViewById(R.id.tripId);
                city = itemView.findViewById(R.id.tripTo);
                creator = itemView.findViewById(R.id.tripCreator);
            }
        }
    }
}