package com.example.travelplanner.api;

import java.util.List;

public class ChatItems {
    private List<User> users;
    private List<User> friends;
    private List<User> friendsRequests;
    private List<Trip> trips;
    private List<Trip> tripsRequests;

    public class User {
        private String _id;
        private String username;
        private String name;

        public String get_id() {
            return _id;
        }

        public String getName() {
            return name;
        }

        public String getUsername() {
            return username;
        }
    }

    public class Trip {
        private String _id;
        private String city;
        private String  creator;
        private String date;

        public String getDate() {
            return date;
        }

        public String getCreator() {
            return creator;
        }

        public String getCity() {
            return city;
        }

        public String get_id() {
            return _id;
        }
    }

    public List<Trip> getTripsRequests() {
        return tripsRequests;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public List<User> getFriendsRequests() {
        return friendsRequests;
    }

    public List<User> getFriends() {
        return friends;
    }

    public List<User> getUsers() {
        return users;
    }
}
