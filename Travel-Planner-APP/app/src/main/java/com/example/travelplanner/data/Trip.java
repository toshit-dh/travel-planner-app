package com.example.travelplanner.data;

import android.util.Log;

public class Trip {
    private String[] tripMates;
    private String date;
    private String returndate;
    private String ticket;
    private String city;
    private String _id;
    private String creator;

    public String getCreator() {
        return creator;
    }

    public String[] getTripMates() {
        return tripMates;
    }



    public String get_id() {
        Log.e("id",_id);
        return _id;
    }

    public String getTicket() {
        return ticket;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getReturndate() {
        return returndate;
    }

    public void setReturndate(String returndate) {
        this.returndate = returndate;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
