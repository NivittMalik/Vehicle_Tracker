package com.example.nivitt.palashsharma;

/**
 * Created by N!V!TT on 20-03-2018.
 */

public class Markers {
    double lat;
    double lon;
    String timestamp;

    public Markers(double lat, double lon, String timestamp) {
        this.setLat(lat);
        this.setLon(lon);
        this.setTimestamp(timestamp);
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

