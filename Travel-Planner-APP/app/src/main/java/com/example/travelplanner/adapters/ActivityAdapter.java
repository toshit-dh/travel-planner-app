package com.example.travelplanner.adapters;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.travelplanner.api.ActivityData;
import com.squareup.picasso.Picasso;
import com.example.travelplanner.R;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelplanner.data.Trip;

import java.util.ArrayList;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {
    private List<ActivityData> activity;
    public ActivityAdapter(List<ActivityData> activty) {
        this.activity = activty;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView category;
        public TextView rank;
        public TextView tags;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textActivityName);
            category = itemView.findViewById(R.id.textCategory);
            rank = itemView.findViewById(R.id.textRank);
            tags = itemView.findViewById(R.id.textAdditionalInfoact);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ActivityData activityData = activity.get(position);
        holder.name.setText(activityData.getName());
        holder.category.setText("Category: " + activityData.getCategory());
        holder.rank.setText("Rank: " + Integer.toString(activityData.getRank()));
        ArrayList<String> activities = activityData.getTags();
        StringBuilder tagsText = new StringBuilder();
        for (String tag : activities) {
            tagsText.append(tag).append("\t");
        }
        holder.tags.setText(tagsText.toString().trim());
    }

    @Override
    public int getItemCount() {
        return activity.size();
    }
}


