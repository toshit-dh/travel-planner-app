package com.example.travelplanner.data;
public class Destination {
    private String name;
    private String description;
    private int imageResourceId;
    private String budget;
    private String dianame;private String detail;private String ticket; private String stay; private String eat; private String visit; private String duration; private String calculation; private String tips; private String time; private String reach; private String currency; private String language;private String Capital;


    public Destination(String name, String description,int imageResourceId,String budget,String detail,String dianame, String ticket, String stay, String eat, String visit, String duration, String calculation, String tips, String time, String reach, String currency, String language,String capital) {
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
        this.budget = budget;
        this.ticket = ticket;
        this.stay = stay;
        this.eat = eat;
        this.visit = visit;
        this.duration = duration;
        this.calculation = calculation;
        this.time = time;
        this.tips = tips;
        this.currency = currency;
        this.reach = reach;
        this.language = language;
        this.detail = detail;
        this.dianame = dianame;
        this.Capital = capital;
    }

    public String getDianame() {
        return dianame;
    }

    public String getDetail() {
        return detail;
    }

    public String getTicket() {
        return ticket;
    }

    public String getStay() {
        return stay;
    }

    public String getEat() {
        return eat;
    }

    public String getVisit() {
        return visit;
    }

    public String getDuration() {
        return duration;
    }

    public String getTips() {
        return tips;
    }

    public String getCalculation() {
        return calculation;
    }

    public String getTime() {
        return time;
    }

    public String getCurrency() {
        return currency;
    }

    public String getReach() {
        return reach;
    }

    public String getLanguage() {
        return language;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBudget() {
        return budget;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getCapital() {
        return Capital;
    }
}
