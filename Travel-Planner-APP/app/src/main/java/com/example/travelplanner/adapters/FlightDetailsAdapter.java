package com.example.travelplanner.adapters;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelplanner.R;
import com.example.travelplanner.api.FlightsData;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.view.View;
import java.util.List;
// FlightDetailsAdapter.java
public class FlightDetailsAdapter extends RecyclerView.Adapter<FlightDetailsAdapter.FlightDetailsViewHolder> {

    private List<FlightsData.FlightData> flightDetailsList;

    public FlightDetailsAdapter(List<FlightsData.FlightData> flightDetailsList) {
        this.flightDetailsList = flightDetailsList;
    }

    @NonNull
    @Override
    public FlightDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flight_details, parent, false);
        return new FlightDetailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightDetailsViewHolder holder, int position) {
        FlightsData.FlightData flightDetails = flightDetailsList.get(position);
        holder.textDuration.setText("Duration: " + flightDetails.getDuration().replace("PT",""));
        holder.textPrice.setText("Price: ₹" + (int)Math.round(flightDetails.getPrice()));
        holder.textDeparture.setText("Departure: " + flightDetails.getDeparture().getAt().replace("T"," ") + "\n" + "Terminal: " + flightDetails.getDeparture().getTerminal());
        holder.textArrival.setText("Arrival: " + flightDetails.getArrival().getAt().replace("T", " ") + "\n" + "Terminal: " + flightDetails.getArrival().getTerminal());
    }

    @Override
    public int getItemCount() {
        return flightDetailsList.size();
    }

    static class FlightDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView textDuration;
        TextView textPrice;
        TextView textDeparture;
        TextView textArrival;

        FlightDetailsViewHolder(View itemView) {
            super(itemView);
            textDuration = itemView.findViewById(R.id.textDuration);
            textPrice = itemView.findViewById(R.id.textPrice);
            textDeparture = itemView.findViewById(R.id.textDeparture);
            textArrival = itemView.findViewById(R.id.textArrival);
        }
    }
}
