package com.example.travelplanner.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplanner.R;
import com.example.travelplanner.api.ChatItems;
import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.api.StringResponse;
import com.example.travelplanner.data.MyPrefs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{
    private List<ChatItems.User> userList;
    private List<ChatItems.Trip> tripList;
    public NotificationAdapter(List<ChatItems.User> userList,List<ChatItems.Trip> tripList){
        this.userList = userList;
        this.tripList = tripList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notif,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> userHeaders = new HashMap<>();
        userHeaders.put("Content-Type", "application/json");
        userHeaders.put("Authorization", MyPrefs.getToken(holder.itemView.getContext()));
        if(position<userList.size()){
            ChatItems.User user = userList.get(position);
            holder.type.setText("Friends Requests: ");
            holder.from.setText(user.getUsername());
            holder.details.setText(user.getName());
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RetrofitInstance.getInstance().apiInterface.onAcceptFriend(user.get_id(),userHeaders).enqueue(new Callback<StringResponse>() {
                        @Override
                        public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                            if(response.isSuccessful()){
                                StringResponse stringResponse = response.body();
                                Toast.makeText(holder.itemView.getContext(), stringResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            }else{
                                Log.e("error",response.errorBody().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<StringResponse> call, Throwable t) {
                            Log.e("servererror",t.getMessage());
                        }
                    });
                }
            });
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RetrofitInstance.getInstance().apiInterface.onRejectFriend(user.get_id(),userHeaders).enqueue(new Callback<StringResponse>() {
                        @Override
                        public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                            if(response.isSuccessful()){
                                StringResponse stringResponse = response.body();
                                Toast.makeText(holder.itemView.getContext(), stringResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            }else{
                                Log.e("error",response.errorBody().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<StringResponse> call, Throwable t) {
                            Log.e("servererror",t.getMessage());
                        }
                    });
                }
            });
        }
        else {
            ChatItems.Trip trip = tripList.get(position-userList.size());
            holder.type.setText("Trip Requests");
            holder.from.setText(trip.getCreator());
            String[] dateday = trip.getDate().split("T");
            String date = dateday[0];
            String[] day = dateday[1].split(":");
            holder.details.setText(date+" "+day[0]+"\n"+trip.getCity());
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.e("counr",Integer.toString(tripList.size()+userList.size()));
        return tripList.size()+userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView type;
        private TextView from;
        private TextView details;
        private ImageView accept;
        private ImageView reject;
        ViewHolder(View itemView){
            super(itemView);
            type = itemView.findViewById(R.id.typeognotif);
            from = itemView.findViewById(R.id.notiffrom);
            details = itemView.findViewById(R.id.notifdetails);
            accept = itemView.findViewById(R.id.typeaccept);
            reject = itemView.findViewById(R.id.typereject);
        }
    }
}
