package com.example.travelplanner.api;

import java.util.ArrayList;

public class FlightsData {
    private String date;
    private ArrayList<FlightData> flightData;

    public ArrayList<FlightData> getFlightData() {
        return flightData;
    }

    public void setFlightData(ArrayList<FlightData> flightData) {
        this.flightData = flightData;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static class FlightData {
        private String date;
        private String duration;
        private double price;
        private Departure departure;
        private Arrival arrival;

        public Arrival getArrival() {
            return arrival;
        }

        public void setArrival(Arrival arrival) {
            this.arrival = arrival;
        }

        public Departure getDeparture() {
            return departure;
        }

        public void setDeparture(Departure departure) {
            this.departure = departure;
        }

        public static class Departure {
            private String iataCode;
            private String terminal;
            private String at;

            public String getAt() {
                return at;
            }

            public void setAt(String at) {
                this.at = at;
            }

            public String getTerminal() {
                return terminal;
            }

            public void setTerminal(String terminal) {
                this.terminal = terminal;
            }

            public String getIataCode() {
                return iataCode;
            }

            public void setIataCode(String iataCode) {
                this.iataCode = iataCode;
            }
        }
        public static class Arrival {
            private String iataCode;
            private String terminal;
            private String at;

            public String getAt() {
                return at;
            }

            public void setAt(String at) {
                this.at = at;
            }

            public String getTerminal() {
                return terminal;
            }

            public void setTerminal(String terminal) {
                this.terminal = terminal;
            }

            public String getIataCode() {
                return iataCode;
            }

            public void setIataCode(String iataCode) {
                this.iataCode = iataCode;
            }
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
