package com.example.travelplanner.data;

public class WeatherData {
   private float tempinC;
   private int pressure;
   private int humidity;
   private int sea_level;
   private int grnd_level;
   private String description;
   private String iconurl;
   private int cloud;
    private double windSpeed;
    private int windDeg;
    private double windGust;
    private String time;

    public double getWindGust() {
        return windGust;
    }

    public String getTime() {
        return time;
    }

    public int getWindDeg() {
        return windDeg;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getCloud() {
        return cloud;
    }

    public String getIconurl() {
        return iconurl;
    }

    public String getDescription() {
        return description;
    }

    public int getGrnd_level() {
        return grnd_level;
    }

    public int getSea_level() {
        return sea_level;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public float getTempinC() {
        return tempinC;
    }

}


