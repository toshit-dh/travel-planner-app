package com.example.travelplanner.adapters;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.TextView;
import com.example.travelplanner.R;
import com.example.travelplanner.api.HotelData;
import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.api.ReviewData;
import com.example.travelplanner.data.MyPrefs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {
    private List<HotelData> hotelList;
    public HotelAdapter(List<HotelData> hotelData){
        this.hotelList = hotelData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HotelData hotelData = hotelList.get(position);
        holder.nameTextView.setText(hotelData.getName());
        holder.hotelIdTextView.setText("Hotel ID: " + hotelData.getHotelId());
        holder.iataTextView.setText("IATA Code: " + hotelData.getIataCode());
        holder.latitudeTextView.setText("Latitude: " + hotelData.getgeoCode().getLatitude());
        holder.longitudeTextView.setText("Longitude: " + hotelData.getgeoCode().getLongitude());
        holder.addressTextView.setText("Country Code: " + hotelData.getAddress().getCountryCode());
        holder.review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> userHeaders = new HashMap<>();
                userHeaders.put("Content-Type", "application/json");
                userHeaders.put("Authorization", MyPrefs.getToken(holder.itemView.getContext()));
                Log.e("id",hotelData.getHotelId());
                RetrofitInstance.getInstance().apiInterface.onHotelReview(hotelData.getHotelId(),userHeaders).enqueue(new Callback<ReviewData>() {
                    @Override
                    public void onResponse(Call<ReviewData> call, Response<ReviewData> response) {
                        if(response.isSuccessful()){
                            ReviewData reviewData = response.body();
                            holder.review.setVisibility(View.GONE);
                            holder.overallReview.setText("Overall: " + Integer.toString(reviewData.getOverallRating()));
                            holder.serviceReview.setText("Service: " + Integer.toString(reviewData.getService()));
                            holder.facilitiesReview.setText("Facility: " + Integer.toString(reviewData.getFacilities()));
                            holder.cateringReview.setText("Caterers: " + Integer.toString(reviewData.getCatering()));
                            holder.staffReview.setText("Staff: " + Integer.toString(reviewData.getStaff()));
                        }
                        else{
                            Log.e("error",response.errorBody().toString());
                        }
                    }
                    @Override
                    public void onFailure(Call<ReviewData> call, Throwable t) {
                        Log.e("servereerror",t.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView hotelIdTextView;
        TextView iataTextView;
        TextView latitudeTextView;
        TextView longitudeTextView;
        TextView addressTextView;
        TextView overallReview;
        TextView serviceReview;
        TextView facilitiesReview;
        TextView cateringReview;
        TextView staffReview;
        Button review;
        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            hotelIdTextView = itemView.findViewById(R.id.hotelIdTextView);
            iataTextView = itemView.findViewById(R.id.iataTextView);
            latitudeTextView = itemView.findViewById(R.id.latitudeTextView);
            longitudeTextView = itemView.findViewById(R.id.longitudeTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            overallReview = itemView.findViewById(R.id.overallreview);
            serviceReview = itemView.findViewById(R.id.servicereview);
            facilitiesReview = itemView.findViewById(R.id.facilityreview);
            cateringReview = itemView.findViewById(R.id.cateringreview);
            staffReview = itemView.findViewById(R.id.staffreview);
            review = itemView.findViewById(R.id.hotel_review);
        }
    }
}
