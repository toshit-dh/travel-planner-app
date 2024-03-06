package com.example.travelplanner.adapters;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.LinearLayout;
import com.example.travelplanner.api.FlightsData;
import com.example.travelplanner.R;
import java.util.List;
import android.view.View;
public class FlightDataAdapter extends RecyclerView.Adapter<FlightDataAdapter.FlightDataViewHolder> {

    private List<FlightsData> flightDataList;

    public FlightDataAdapter(List<FlightsData> flightDataList) {
        this.flightDataList = flightDataList;
    }

    @NonNull
    @Override
    public FlightDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flight_data, parent, false);
        return new FlightDataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightDataViewHolder holder, int position) {
        FlightsData flightData = flightDataList.get(position);
        holder.textDate.setText("Date: " + flightData.getDate());
        holder.textSrc.setText("Source: " + flightData.getFlightData().get(position).getDeparture().getIataCode());
        holder.textDes.setText("Destination: " + flightData.getFlightData().get(position).getArrival().getIataCode());

        // Set up the second-level RecyclerView
        FlightDetailsAdapter detailsAdapter = new FlightDetailsAdapter(flightData.getFlightData());
        holder.recyclerViewFlightDetails.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(),LinearLayoutManager.HORIZONTAL,false));
        holder.recyclerViewFlightDetails.setAdapter(detailsAdapter);
    }

    @Override
    public int getItemCount() {
        return flightDataList.size();
    }

    static class FlightDataViewHolder extends RecyclerView.ViewHolder {
        TextView textDate;
        TextView textSrc;
        TextView textDes;
        RecyclerView recyclerViewFlightDetails;

        FlightDataViewHolder(View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.textDate);
            textSrc = itemView.findViewById(R.id.textSrc);
            textDes = itemView.findViewById(R.id.textDes);
            recyclerViewFlightDetails = itemView.findViewById(R.id.recyclerViewFlightDetails);
        }
    }
}
