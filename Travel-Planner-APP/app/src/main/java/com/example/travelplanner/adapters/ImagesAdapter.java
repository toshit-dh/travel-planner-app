package com.example.travelplanner.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplanner.R;
import com.example.travelplanner.api.RetrofitInstance;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder>{
    private ArrayList<String> imageUrl;
    public ImagesAdapter(ArrayList<String> imageUrl){
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_images,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String image = imageUrl.get(position);
        String fullImageUrl = RetrofitInstance.IPAddress + image;
        Log.e("image",fullImageUrl);
        Picasso.get().load(fullImageUrl)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.images, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.i("Picasso", "Image loaded successfully");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("nosucc", e.getMessage() + e.toString());
                    }
                });

    }

    @Override
    public int getItemCount() {
        return imageUrl.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView images;
        ViewHolder(View itemView){
            super(itemView);
            images = itemView.findViewById(R.id.tripimagees);
        }
    }
}
