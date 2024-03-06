package com.example.travelplanner.adapters;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.api.StringResponse;
import com.example.travelplanner.data.MyPrefs;
import com.example.travelplanner.data.WeatherData;
import com.squareup.picasso.Picasso;
import com.example.travelplanner.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelplanner.data.Trip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTripsAdapter extends RecyclerView.Adapter<MyTripsAdapter.ViewHolder> {
    private List<Trip> trips;
    public MyTripsAdapter(List<Trip> trips) {
        this.trips = trips;
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        public TextView departureDateTimeTextView;
        public TextView arrivalDateTimeTextView;
        public TextView arrivalCity;
        public ImageView tripImageView;
        public Button alarm;
        public Button cancel;
        private TextView search;
        private RecyclerView weatherRecycler;
        private TextView tripid;
        private TextView tripCreator;
        private TextView seemore;
        private TextView seeless;
        private TextView tripmates;
        private LinearLayout tripsee;
        public ViewHolder(View itemView) {
            super(itemView);
            departureDateTimeTextView = itemView.findViewById(R.id.departureDateTimeTextView);
            arrivalDateTimeTextView = itemView.findViewById(R.id.arrivalDateTimeTextView);
            arrivalCity = itemView.findViewById(R.id.arrivalcity);
            tripImageView = itemView.findViewById(R.id.tripImageView);
            alarm = itemView.findViewById(R.id.setAlarm);
            cancel = itemView.findViewById(R.id.cancelTrip);
            search = itemView.findViewById(R.id.searchWeather);
            tripCreator = itemView.findViewById(R.id.tripcreator);
            tripid = itemView.findViewById(R.id.tripid);
            seeless = itemView.findViewById(R.id.tripseeless);
            seemore = itemView.findViewById(R.id.tripseemore);
            tripsee = itemView.findViewById(R.id.tripseelessmore);
            tripmates = itemView.findViewById(R.id.tripmates);
            weatherRecycler = itemView.findViewById(R.id.wearecyclerView);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    if (index != RecyclerView.NO_POSITION) {
                        String id = trips.get(index).get_id();
                        Log.e("hu",id);
                        Map<String,String> userHeaders = new HashMap<>();
                        userHeaders.put("Content-Type", "application/json");
                        userHeaders.put("Authorization", MyPrefs.getToken(itemView.getContext()));
                        RetrofitInstance.getInstance().apiInterface.onCancelTrip(id,userHeaders).enqueue(new Callback<StringResponse>() {
                            @Override
                            public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                                if(response.isSuccessful()){
                                    trips.remove(index);
                                    notifyItemRemoved(index);
                                    StringResponse response1 = response.body();
                                    Toast.makeText(itemView.getContext(),response1.getMsg(),Toast.LENGTH_SHORT).show();
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
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_card, parent, false);
        return new ViewHolder(view);
    }

    // Override onBindViewHolder to bind data to views
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.tripid.setText(trip.get_id());
        holder.tripCreator.setText("Trip Creator: "+trip.getCreator());
        String dateAndTime = trip.getDate().substring(0, 10) + " " + trip.getDate().substring(11, 16);
        String retdateAndTime = trip.getReturndate().substring(0, 10) + " " + trip.getReturndate().substring(11, 16);
        holder.departureDateTimeTextView.setText("Departure: "+ dateAndTime);
        holder.arrivalDateTimeTextView.setText("Arrival: "+retdateAndTime);
        holder.arrivalCity.setText("Arrival City: "+trip.getCity());
        StringBuilder tripMatesText = new StringBuilder();
        for (String tripmate: trip.getTripMates()) {
            tripMatesText.append(tripmate).append("\n");
        }
        holder.tripmates.setText(tripMatesText.toString());
        String tic = trip.getTicket();
        String url = RetrofitInstance.IPAddress + tic;
        Log.e("rggv",url);
        Picasso.get().load(url).into(holder.tripImageView);
        holder.seemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.seemore.setVisibility(View.GONE);
                holder.tripsee.setVisibility(View.VISIBLE);
            }
        });
        holder.seeless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.seemore.setVisibility(View.VISIBLE);
                holder.tripsee.setVisibility(View.GONE);
            }
        });
        holder.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> userHeaders = new HashMap<>();
                userHeaders.put("Content-Type", "application/json");
                userHeaders.put("Authorization", MyPrefs.getToken(view.getContext()));
                RetrofitInstance.getInstance().apiInterface.onWeatherData(trip.getCity(),userHeaders).enqueue(new Callback<List<WeatherData>>() {
                    @Override
                    public void onResponse(Call<List<WeatherData>> call, Response<List<WeatherData>> response) {
                        if(response.isSuccessful()){
                            List<WeatherData> weatherData = response.body();
                            WeatherAdapter weatherAdapter = new WeatherAdapter(weatherData);
                            weatherAdapter = new WeatherAdapter(weatherData);
                            holder.search.setVisibility(View.GONE);
                            holder.weatherRecycler.setVisibility(View.VISIBLE);
                            holder.weatherRecycler.setAdapter(weatherAdapter);
                            holder.weatherRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false));
                        }
                        else {
                            Log.e("error",response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<WeatherData>> call, Throwable t) {
                        Log.e("servererror","weather");
                    }
                });
            }
        });
    }

    // Override getItemCount to return the number of items
    @Override
    public int getItemCount() {
        return trips.size();
    }
}

