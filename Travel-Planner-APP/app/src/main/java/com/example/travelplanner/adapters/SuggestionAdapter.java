package com.example.travelplanner.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelplanner.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.api.StringResponse;
import com.example.travelplanner.api.SuggestionData;
import com.example.travelplanner.data.MyPrefs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {
    private Boolean mySuggestion;
    private List<SuggestionData> suggestionData;
    public SuggestionAdapter(List<SuggestionData> suggestionData,Boolean mySuggestion){
        this.suggestionData = suggestionData;
        this.mySuggestion = mySuggestion;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(!mySuggestion){
            holder.button.setVisibility(View.GONE);
        }
        else {
            holder.like.setEnabled(false);
        }
        SuggestionData suggestionDataItem = suggestionData.get(position);
        if(suggestionDataItem.getVoted()){
            holder.like.setImageResource(R.drawable.heart_fill);
        }
        else{
            holder.like.setImageResource(R.drawable.heart_border);
        }
        holder.tag.setText(suggestionDataItem.getTag());
        String city = suggestionDataItem.getLoc().getCity();
        String CapitalizedCity = city.substring(0,1).toUpperCase() + city.substring(1);
        holder.city.setText(CapitalizedCity);
        Log.e("gf",suggestionDataItem.getLoc().getCity());
        holder.country.setText(suggestionDataItem.getLoc().getCountry());
        holder.msg.setText(suggestionDataItem.getMsg());
        holder.date.setText(suggestionDataItem.getDate());
        Log.e("date",suggestionDataItem.getDate());
        holder.by.setText("~"+suggestionDataItem.getBy());
        int sentiment = suggestionDataItem.getSentiment();
        if(sentiment > 0){
            holder.sentiment.setText("Sentiment: Positive");
        }
        else if(sentiment < 0){
            holder.sentiment.setText("Sentiment: Negative");
        }
        else{
            holder.sentiment.setText("Sentiment: Neutral");
        }
        holder.votes.setText(Integer.toString(suggestionDataItem.getVotes()));
        Map<String, String> userHeaders = new HashMap<>();
        userHeaders.put("Content-Type", "application/json");
        userHeaders.put("Authorization", MyPrefs.getToken(holder.itemView.getContext()));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    RetrofitInstance.getInstance().apiInterface.onRemoveSuggestion(suggestionDataItem.getId(),userHeaders).enqueue(new Callback<StringResponse>() {
                        @Override
                        public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                            if(response.isSuccessful()){
                                StringResponse stringResponse= response.body();
                                Toast.makeText(view.getContext(),stringResponse.getMsg(),Toast.LENGTH_SHORT).show();
                                suggestionData.remove(adapterPosition);
                                notifyItemRemoved(adapterPosition);
                            }
                            else{
                                Log.e("error",response.errorBody().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<StringResponse> call, Throwable t) {
                            Log.e("servererror",t.getMessage());
                        }
                    });
                }
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(suggestionDataItem.getVoted()){
                    RetrofitInstance.getInstance().apiInterface.onRemoveVote(suggestionDataItem.getId(),userHeaders).enqueue(new Callback<StringResponse>() {
                        @Override
                        public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                            if(response.isSuccessful()){
                                StringResponse stringResponse = response.body();
                                if(stringResponse.getStatus()){
                                    suggestionDataItem.setVoted(false);
                                    holder.like.setImageResource(R.drawable.heart_border);
                                    suggestionDataItem.setVotes(suggestionDataItem.getVotes()-1);
                                    notifyItemChanged(position);
                                }
                            }
                            else{
                                Log.e("error",response.errorBody().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<StringResponse> call, Throwable t) {
                            Log.e("servererror",t.getMessage());
                        }
                    });
                }else {
                    RetrofitInstance.getInstance().apiInterface.onVotingSuggestion(suggestionDataItem.getId(),userHeaders).enqueue(new Callback<StringResponse>() {
                        @Override
                        public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                            if(response.isSuccessful()){
                                StringResponse stringResponse = response.body();
                                if(stringResponse.getStatus()){
                                    suggestionDataItem.setVoted(true);
                                    holder.like.setImageResource(R.drawable.heart_fill);
                                    suggestionDataItem.setVotes(suggestionDataItem.getVotes()+1);
                                    notifyItemChanged(position);
                                }
                            }else {
                                Log.e("error",response.errorBody().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<StringResponse> call, Throwable t) {
                            Log.e("servererror",t.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestionData.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tag;
        private TextView city;
        private TextView country;
        private TextView msg;
        private TextView date;
        private TextView by;
        private TextView votes;
        private TextView sentiment;
        private ImageView like;
        private Button button;
        public ViewHolder(View itemView){
            super(itemView);
            tag = itemView.findViewById(R.id.itemsuggtag);
            city = itemView.findViewById(R.id.itemsuggcity);
            country = itemView.findViewById(R.id.itemsuggcountry);
            msg = itemView.findViewById(R.id.suggMsg);
            date = itemView.findViewById(R.id.suggtime);
            by = itemView.findViewById(R.id.suggby);
            votes = itemView.findViewById(R.id.votes);
            like = itemView.findViewById(R.id.mylike);
            sentiment = itemView.findViewById(R.id.sentiment);
            button = itemView.findViewById(R.id.removesugg);
        }
    }
}
