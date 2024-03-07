package com.example.travelplanner.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplanner.R;
import com.example.travelplanner.api.ItenaryData;

import java.util.ArrayList;

public class ItenaryAdapter extends RecyclerView.Adapter<ItenaryAdapter.ViewHolder>{
    private ArrayList<ItenaryData.DayPlan> dayPlans;
    private ArrayList<ItenaryData.Activity> intrestingactivity;
    public ItenaryAdapter(ArrayList<ItenaryData.DayPlan> dayPlans, ArrayList<ItenaryData.Activity> intrestingactivity){
        this.dayPlans = dayPlans;
        this.intrestingactivity = intrestingactivity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_itenary,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItenaryData.DayPlan dayPlan;
        ItenaryData.Activity intrestingActivity;
        if(position<dayPlans.size()){
            dayPlan = dayPlans.get(position);
            holder.dayplanner.setVisibility(View.VISIBLE);
            holder.intrestingActivity.setVisibility(View.GONE);
            holder.whichDay.setText("Day "+Integer.toString(position+1));
            holder.morningActivity.setText("Activity: "+dayPlan.getMorning().getActivity());
            holder.morningCost.setText("Cost: "+dayPlan.getMorning().getCost());
            holder.afternoonActivity.setText("Activity: "+dayPlan.getAfternoon().getActivity());
            holder.afternoonCost.setText("Cost: "+dayPlan.getAfternoon().getCost());
            holder.eveningActivity.setText("Activity: "+dayPlan.getEvening().getActivity());
            holder.eveningCost.setText("Cost: "+dayPlan.getEvening().getCost());
        }else {
            Log.e("hvy"," chvvj");
            holder.dayplanner.setVisibility(View.GONE);
            holder.intrestingActivity.setVisibility(View.VISIBLE);
            IntrestingActivityAdapter intrestingActivityAdapter = new IntrestingActivityAdapter(intrestingactivity);
            holder.recyclerView.setAdapter(intrestingActivityAdapter);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        }

    }

    @Override
    public int getItemCount() {
        return dayPlans.size()+1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView morningActivity;
        private TextView morningCost;
        private TextView afternoonActivity;
        private TextView afternoonCost;
        private TextView eveningActivity;
        private TextView eveningCost;
        private TextView whichDay;
        private LinearLayout intrestingActivity;
        private LinearLayout dayplanner;
        private RecyclerView recyclerView;
        ViewHolder(View itemView){
            super(itemView);
            morningActivity = itemView.findViewById(R.id.morningactivity);
            morningCost = itemView.findViewById(R.id.morningcost);
            afternoonActivity = itemView.findViewById(R.id.afternoonactivity);
            afternoonCost = itemView.findViewById(R.id.afternooncost);
            eveningActivity = itemView.findViewById(R.id.eveningactivity);
            eveningCost = itemView.findViewById(R.id.eveningcost);
            intrestingActivity = itemView.findViewById(R.id.iactivitieslinear);
            whichDay = itemView.findViewById(R.id.whichday);
            dayplanner = itemView.findViewById(R.id.dayplanlinear);
            recyclerView = itemView.findViewById(R.id.intrestingactivityrecycler);
        }
    }
    static class IntrestingActivityAdapter extends RecyclerView.Adapter<IntrestingActivityAdapter.ViewHolder>{
        private ArrayList<ItenaryData.Activity> intrestingactivity;
        public IntrestingActivityAdapter(ArrayList<ItenaryData.Activity> intrestingactivity){
            this.intrestingactivity = intrestingactivity;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_iactivity,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ItenaryData.Activity activity = intrestingactivity.get(position);
            holder.activities.setText(activity.getActivity()+" "+"Cost: "+activity.getCost());
        }

        @Override
        public int getItemCount() {
            return intrestingactivity.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder{
            private TextView activities;
            ViewHolder(View itemView){
                super(itemView);
                activities = itemView.findViewById(R.id.iactivity);
            }
        }
    }
}
