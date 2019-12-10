package com.kotizm.testworkgbksoft.ui.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.kotizm.testworkgbksoft.R;
import com.kotizm.testworkgbksoft.model.PointOnMap;

@SuppressLint("InflateParams")
public class MarkerClusterRenderer extends DefaultClusterRenderer<PointOnMap>
        implements ClusterManager.OnClusterClickListener<PointOnMap>,
        ClusterManager.OnClusterItemInfoWindowClickListener<PointOnMap>, GoogleMap.OnInfoWindowClickListener {

    private Context context;
    private GoogleMap googleMap;
    private LayoutInflater layoutInflater;
    private final IconGenerator clusterIconGenerator;

    private final View clusterItemView;

    MarkerClusterRenderer(@NonNull Context context, GoogleMap map, ClusterManager<PointOnMap> clusterManager) {
        super(context, map, clusterManager);

        this.googleMap = map;
        this.context = context;

        layoutInflater = LayoutInflater.from(context);
        clusterItemView = layoutInflater.inflate(R.layout.single_cluster_marker_view, null);

        clusterIconGenerator = new IconGenerator(context);
        Drawable drawable = ContextCompat.getDrawable(context, android.R.color.transparent);
        clusterIconGenerator.setBackground(drawable);
        clusterIconGenerator.setContentView(clusterItemView);

        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
        clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomClusterItemInfoView());

        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
        clusterManager.setOnClusterClickListener(this);
    }

    @Override
    protected void onBeforeClusterItemRendered(PointOnMap item, MarkerOptions markerOptions) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.snowman_christmas);
        Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        markerOptions.icon(smallMarkerIcon);
        markerOptions.title(item.getTitle());
    }

    @Override
    protected String getClusterText(int bucket) {
        return String.valueOf(bucket);
    }

    @Override
    protected int getBucket(Cluster<PointOnMap> cluster) {
        return cluster.getSize();
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<PointOnMap> cluster, MarkerOptions markerOptions) {
        TextView singleClusterMarkerTextView = clusterItemView.findViewById(R.id.singleClusterMarkerTextView);
        singleClusterMarkerTextView.setText(String.valueOf(cluster.getSize()));

        Bitmap icon = clusterIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected void onClusterItemRendered(PointOnMap clusterItem, Marker marker) {
        marker.setTag(clusterItem);
    }

    @Override
    public boolean onClusterClick(Cluster<PointOnMap> cluster) {
        if (cluster == null) return false;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (PointOnMap points : cluster.getItems())
            builder.include(points.getPosition());
        LatLngBounds bounds = builder.build();
        try {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.isInfoWindowShown()) marker.hideInfoWindow();
    }

    @Override
    public void onClusterItemInfoWindowClick(PointOnMap pointOnMap) {

    }

    private class MyCustomClusterItemInfoView implements GoogleMap.InfoWindowAdapter {

        private final View clusterItemView;

        MyCustomClusterItemInfoView() {
            clusterItemView = layoutInflater.inflate(R.layout.marker_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            PointOnMap point = (PointOnMap) marker.getTag();
            if (point == null) return clusterItemView;

            TextView itemNameTextView = clusterItemView.findViewById(R.id.itemNameTextView);
            itemNameTextView.setText(marker.getTitle());

            return clusterItemView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
}