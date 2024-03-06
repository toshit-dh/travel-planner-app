package com.example.travelplanner.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplanner.R;
import com.example.travelplanner.data.TripChatsImages;

import java.util.ArrayList;
import java.util.Objects;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder>{
    private ArrayList<TripChatsImages.Message> messages;
    public MessagesAdapter(ArrayList<TripChatsImages.Message> messages){
        this.messages = messages;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TripChatsImages.Message message = messages.get(position);
        holder.msg.setText(message.getMessage());
        if(Objects.equals(message.getIsType(), "text")){
            holder.textOrImage.setImageResource(R.drawable.baseline_short_text_24);
        }else{
            holder.textOrImage.setImageResource(R.drawable.photo);
        }
        if(message.getFromSelf()){
            holder.from.setText("You");
        }
        else{
            holder.from.setText(message.getFromWho());
        }
//
    }
    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView textOrImage;
        private TextView from;
        private TextView msg;
        private CardView groupCard;
        ViewHolder(View itemView){
            super(itemView);
            textOrImage = itemView.findViewById(R.id.textorimage);
            from = itemView.findViewById(R.id.fromname);
            msg = itemView.findViewById(R.id.msgtext);
            groupCard = itemView.findViewById(R.id.groupCardView);
        }
    }
}
