package com.example.travelplanner.adapters;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{
    private List<ChatItems.User> users;
    private String usedWhere;
    public UsersAdapter(List<ChatItems.User> users,String usedWhere){
        this.users = users;
        this.usedWhere = usedWhere;
    }
    public void filterList(List<ChatItems.User> filteredUsers) {
        users = filteredUsers;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_friend,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatItems.User user = users.get(position);
        if(usedWhere == "Users"){
            holder.addRequest.setImageResource(R.drawable.add_friend);
        }
        else if(usedWhere == "Friends"){
            holder.addRequest.setVisibility(View.GONE);
        }
        holder.name.setText(user.getName());
        holder.username.setText(user.getUsername());
        holder.addRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> userHeaders = new HashMap<>();
                userHeaders.put("Content-Type", "application/json");
                userHeaders.put("Authorization", MyPrefs.getToken(view.getContext()));
                RetrofitInstance.getInstance().apiInterface.onAddFriend(user.get_id(),userHeaders).enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                        if(response.isSuccessful()){
                            StringResponse msg = response.body();
                            Toast.makeText(view.getContext(),msg.getMsg(),Toast.LENGTH_SHORT).show();
                            holder.addRequest.setImageResource(R.drawable.time);
                        }
                        else{
                            if(response.code() == 404 || response.code() == 400){
                                Toast.makeText(view.getContext(),"Request Not Sent",Toast.LENGTH_SHORT).show();
                            }
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

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView username;
        private ImageView addRequest;
        public ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.usersname);
            username = itemView.findViewById(R.id.userusername);
            addRequest = itemView.findViewById(R.id.addrequest);
        }
    }
}
