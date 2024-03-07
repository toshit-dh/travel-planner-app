package com.example.travelplanner.api;

public class SuggestionData {
    private String id;

    public String getId() {
        return id;
    }

    private String by;
    private String msg;
    private String tag;
    private String date;
    private int sentiment;
    private int votes;
    private Boolean isVoted;

    public void setVoted(Boolean voted) {
        isVoted = voted;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public Boolean getVoted() {
        return isVoted;
    }

    public int getSentiment() {
        return sentiment;
    }

    public int getVotes() {
        return votes;
    }

    public String getDate() {
        return date;
    }

    private loc loc;
    public class loc{
        private String city;
        private String country;

        public String getCountry() {
            return country;
        }

        public String getCity() {
            return city;
        }
    }

    public SuggestionData.loc getLoc() {
        return loc;
    }

    public String getTag() {
        return tag;
    }

    public String getMsg() {
        return msg;
    }

    public String getBy() {
        return by;
    }
}
