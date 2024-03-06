package com.example.travelplanner.api;

import java.util.ArrayList;

public class ActivityData {
    private String name;
    private String category;
    private int rank;
    private ArrayList<String> tags;

    public ArrayList<String> getTags() {
        return tags;
    }

    public int getRank() {
        return rank;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }
}
