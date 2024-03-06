package com.example.travelplanner.api;

public class HotelData {
    private String iataCode;
    private String name;
    private String hotelId;
    private geoCode geoCode;
    private address address;


    public String getHotelId() {
        return hotelId;
    }

    public String getName() {
        return name;
    }

    public String getIataCode() {
        return iataCode;
    }
    public geoCode getgeoCode(){
        return geoCode;
    }
    public address getAddress(){
        return address;
    }
    public static class geoCode {
        private double latitude;
        private double longitude;
        public double getLongitude() {
            return longitude;
        }
        public double getLatitude() {
            return latitude;
        }
    }
    public static class address {
        private String countryCode;
        public String getCountryCode() {
            return countryCode;
        }
    }
}

