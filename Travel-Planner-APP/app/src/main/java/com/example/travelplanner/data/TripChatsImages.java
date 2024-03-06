package com.example.travelplanner.data;

import java.util.ArrayList;

public class TripChatsImages{
    private ArrayList<Message> message;
    private ArrayList<String> imageUrl;

    public ArrayList<String> getImages() {
        return imageUrl;
    }

    public ArrayList<Message> getMessage() {
        return message;
    }

    public static class Message {
        private String fromWho;
        private String message;
        private String isType;
        private Boolean fromSelf;
        public Message(String fromWho,String message,String isType,Boolean fromSelf) {
            this.fromWho = fromWho;
            this.message = message;
            this.isType = isType;
            this.fromSelf = fromSelf;
        }

        public Boolean getFromSelf() {
            return fromSelf;
        }

        public String getIsType() {
            return isType;
        }

        public String getMessage() {
            return message;
        }

        public String getFromWho() {
            return fromWho;
        }
    }

}
