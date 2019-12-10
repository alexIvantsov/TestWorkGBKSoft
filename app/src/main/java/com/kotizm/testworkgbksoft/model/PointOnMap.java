package com.kotizm.testworkgbksoft.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class PointOnMap implements ClusterItem {

    private final String name;
    private final LatLng latLng;

    public PointOnMap(String name, LatLng latLng) {
        this.name = name;
        this.latLng = latLng;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return "";
    }
}