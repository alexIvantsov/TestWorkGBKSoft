package com.kotizm.testworkgbksoft.model;

import org.parceler.Parcel;

@Parcel(Parcel.Serialization.BEAN)
public class FirebaseData {

    String id;

    String name;

    double  latitude;

    double  longitude;

    public FirebaseData() {
    }

    public FirebaseData(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}