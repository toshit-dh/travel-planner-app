package com.example.travelplanner.data;

public class RawMessage {
    private String message;
    private String from;
    private String fromWho;
    private String isType;
    private String fromWhoId;
    private Boolean fromSelf;

    public void setFromSelf(Boolean fromSelf) {
        this.fromSelf = fromSelf;
    }

    public Boolean getFromSelf() {
        return fromSelf;
    }

    public String getFromWhoId() {
        return fromWhoId;
    }

    public String getIsType() {
        return isType;
    }

    public String getFromWho() {
        return fromWho;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }
}
