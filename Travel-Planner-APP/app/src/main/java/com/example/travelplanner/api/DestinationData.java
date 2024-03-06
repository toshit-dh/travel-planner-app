package com.example.travelplanner.api;

import java.util.ArrayList;

public class DestinationData {
    private String name;
    private String description;
    private String amount;
    private String currencyCode;
    private ArrayList<String> pictures;
    private String bookingLink;
    private String minimumDuration;

    public String getMinimumDuration() {
        return minimumDuration;
    }

    public String getBookingLink() {
        return bookingLink;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
