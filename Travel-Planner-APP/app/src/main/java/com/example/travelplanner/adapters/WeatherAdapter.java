package com.example.travelplanner.adapters;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplanner.R;
import com.example.travelplanner.data.WeatherData;

import java.util.List;
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private List<WeatherData> weatherList;

    public WeatherAdapter(List<WeatherData> weatherList) {
        this.weatherList = weatherList;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewTemperature,textViewPressure,
                textViewSeaLevel, textViewGroundLevel, textViewHumidity, textViewClouds,
                textViewWindSpeed, textViewWindDegree, textViewWindGust,textweather;
        ImageView imgViewWeatherDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTemperature = itemView.findViewById(R.id.textViewTemperature);
            textweather = itemView.findViewById(R.id.textdescweather);
            textViewPressure = itemView.findViewById(R.id.textViewPressure);
            textViewSeaLevel = itemView.findViewById(R.id.textViewSeaLevel);
            textViewGroundLevel = itemView.findViewById(R.id.textViewGroundLevel);
            textViewHumidity = itemView.findViewById(R.id.textViewHumidity);
            imgViewWeatherDescription = itemView.findViewById(R.id.textViewWeatherDescription);
            textViewClouds = itemView.findViewById(R.id.textViewClouds);
            textViewWindSpeed = itemView.findViewById(R.id.textViewWindSpeed);
            textViewWindDegree = itemView.findViewById(R.id.textViewWindDegree);
            textViewWindGust = itemView.findViewById(R.id.textViewWindGust);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherData weatherData = weatherList.get(position);

        // Set data to views
        holder.textViewDate.setText(weatherData.getTime());
        String temp = Double.toString(Math.round(weatherData.getTempinC()));
        holder.textViewTemperature.setText("Temperature: " + temp+ "°C");
        holder.textViewPressure.setText("Pressure: " + weatherData.getPressure());
        holder.textViewSeaLevel.setText("Sea Level: " + weatherData.getSea_level());
        holder.textViewGroundLevel.setText("Ground Level: " + weatherData.getGrnd_level());
        holder.textViewHumidity.setText("Humidity: " + weatherData.getHumidity());
        Picasso.get().load(weatherData.getIconurl()).into(holder.imgViewWeatherDescription);
        holder.textweather.setText(weatherData.getDescription());
        holder.textViewClouds.setText("Clouds: " + weatherData.getCloud());
        holder.textViewWindSpeed.setText("Wind Speed: " + weatherData.getWindSpeed());
        holder.textViewWindDegree.setText("Wind Degree: " + weatherData.getWindDeg());
        holder.textViewWindGust.setText("Wind Gust: " + weatherData.getWindGust());

    }
    @Override
    public int getItemCount() {
        return weatherList.size();
    }
}
