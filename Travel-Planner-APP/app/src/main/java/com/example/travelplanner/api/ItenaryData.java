package com.example.travelplanner.api;

import java.util.ArrayList;

public class ItenaryData {
    public static class Activity {
        private String activity;
        private String cost;

        public String getCost() {
            return cost;
        }

        public String getActivity() {
            return activity;
        }
    }
    public static class DayPlan{
        private Activity morning;
        private Activity afternoon;
        private Activity evening;

        public Activity getEvening() {
            return evening;
        }

        public Activity getAfternoon() {
            return afternoon;
        }

        public Activity getMorning() {
            return morning;
        }
    }
    private ArrayList<DayPlan> dayPlan;
    private ArrayList<Activity> intrestingActivities;
    private String message;

    public String getMessage() {
        return message;
    }

    public ArrayList<Activity> getIntrestingActivities() {
        return intrestingActivities;
    }

    public ArrayList<DayPlan> getDayPlan() {
        return dayPlan;
    }
}
