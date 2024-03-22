package com.example.travelplanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.travelplanner.R;
import com.example.travelplanner.api.DestinationData;
import com.example.travelplanner.api.FlightsData;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationDataViewHolder> {
    private List<DestinationData> destinationsData;
    public DestinationAdapter(List<DestinationData> destinationsData){
        this.destinationsData = destinationsData;
        Collections.reverse(this.destinationsData);
    }
    @NonNull
    @Override
    public DestinationDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destination, parent, false);
        return new DestinationDataViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull DestinationDataViewHolder holder, int position) {
        DestinationData destinationData = destinationsData.get(position);
        if(destinationData.getAmount()!= null && destinationData.getCurrencyCode()!=null){
            holder.amount.setText(destinationData.getAmount() + " " + destinationData.getCurrencyCode());
        }
        else{
            holder.amount.setText("Not Available");
        }
        holder.minDuration.setText(destinationData.getMinimumDuration());
        Context context = holder.itemView.getContext();
        ImagePagerAdapter adapter = new ImagePagerAdapter(context, destinationData.getPictures(),holder.images,destinationData.getName());
        holder.images.setAdapter(adapter);

        holder.bookTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(destinationData.getBookingLink()));
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return destinationsData.size();
    }
    static class DestinationDataViewHolder extends RecyclerView.ViewHolder {
        private ViewPager images;
        private Button bookTrip;
        private TextView minDuration;
        private TextView amount;
        DestinationDataViewHolder(View itemView){
            super(itemView);
            images = itemView.findViewById(R.id.viewPagertripimage);
            bookTrip = itemView.findViewById(R.id.booktrip);
            minDuration = itemView.findViewById(R.id.minduration);
            amount = itemView.findViewById(R.id.amount4trip);
        }
    }
    static class ImagePagerAdapter extends PagerAdapter {
        private Context context;
        private List<String> images;
        private ViewPager viewPager;
        private String name;
        ImagePagerAdapter(Context context, ArrayList<String> images,ViewPager viewPager,String name) {
            this.context = context;
            this.viewPager = viewPager;
            this.images = images;
            this.name = name;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            // Create a RelativeLayout as the root layout
            RelativeLayout layout = new RelativeLayout(context);
            layout.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            ));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.get().load(images.get(position)).into(imageView);
            TextView textView = new TextView(context);
            textView.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            ));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView.setText(name);
            layout.addView(imageView);
            layout.addView(textView);
            container.addView(layout);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(position!=0){
                        textView.setVisibility(View.GONE);
                    }
                    else {
                        textView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    // Not needed for your purpose but can be useful for other scenarios
                }
            });
            return layout;
        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}