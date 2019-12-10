package com.kotizm.testworkgbksoft.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;
import com.kotizm.testworkgbksoft.R;
import com.kotizm.testworkgbksoft.firebase.database.write.IFirebaseWriteView;
import com.kotizm.testworkgbksoft.firebase.database.write.data.FirebaseWriteData;
import com.kotizm.testworkgbksoft.firebase.database.write.presenter.FirebaseWritePresenter;
import com.kotizm.testworkgbksoft.firebase.database.write.presenter.IFirebaseWritePresenter;
import com.kotizm.testworkgbksoft.model.FirebaseData;
import com.kotizm.testworkgbksoft.model.PointOnMap;
import com.kotizm.testworkgbksoft.utils.dialog.addpoint.AddPointDialogPresenter;
import com.kotizm.testworkgbksoft.utils.dialog.addpoint.AddPointDialogReceiveData;
import com.kotizm.testworkgbksoft.utils.dialog.addpoint.IAddPointDialogPresenter;
import com.kotizm.testworkgbksoft.utils.dialog.addpoint.IAddPointDialogView;
import com.kotizm.testworkgbksoft.utils.permission.Permission;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment implements
        OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMapLongClickListener, IAddPointDialogView, IFirebaseWriteView {

    private int position;
    private IFirebaseWritePresenter writePresenter;
    private IAddPointDialogPresenter dialogPresenter;

    private GoogleMap mMap;
    private List<FirebaseData> point;
    private ClusterManager<PointOnMap> clusterManager;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private LatLng newPointLatLng;
    private AppCompatActivity activity;
    private boolean mPermissionDenied = false;

    private static final int DEFAULT_ZOOM = 15;
    private static final String LOG_MESSAGE = "log_message";
    private static final String POINT = "point_from_firebase";
    private static final String POINT_POSITION = "point_position";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public MapFragment() {
    }

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        activity = (AppCompatActivity) Objects.requireNonNull(getActivity());

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        if (getArguments() != null) {
            position = getArguments().getInt(POINT_POSITION, -1);
            point = Parcels.unwrap(getArguments().getParcelable(POINT));
        }
        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Permission.requestPermission(activity, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else {
            if (mMap != null && !mPermissionDenied) showMap(mMap);
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        newPointLatLng = latLng;

        if (dialogPresenter == null) {
            dialogPresenter = new AddPointDialogPresenter(this, new AddPointDialogReceiveData(activity));
        }
        dialogPresenter.requestData();
    }

    @Override
    public void setPointName(String pointName) {
        if (writePresenter == null) {
            writePresenter = new FirebaseWritePresenter(this, new FirebaseWriteData());
        }
        writePresenter.writeItem(new FirebaseData(null, pointName, newPointLatLng.latitude, newPointLatLng.longitude));
    }

    @Override
    public void onWriteSuccess(String id, FirebaseData item) {
        addPointOnMap(item);
        Toast.makeText(activity, "Point " + item.getName() + " save!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWriteFailure(Throwable throwable) {
        Log.e(LOG_MESSAGE, getString(R.string.firebase_auth_error) + throwable);
    }

    private void addPointOnMap(FirebaseData item) {
        LatLng location = new LatLng(item.getLatitude(), item.getLongitude());
        point.add(item);

        Collections.sort(point, new Comparator<FirebaseData>() {
            @Override
            public int compare(FirebaseData firebaseData, FirebaseData t1) {
                return firebaseData.getName().compareTo((t1).getName());
            }
        });
        clusterManager.addItem(new PointOnMap(item.getName(), location));
        clusterManager.cluster();
    }

    private void setUpClusterManager(GoogleMap googleMap) {
        List<PointOnMap> itemOnMap = getItemOnMap();
        clusterManager = new ClusterManager<>(activity, googleMap);

        MarkerClusterRenderer markerClusterRenderer = new MarkerClusterRenderer(activity, googleMap, clusterManager);
        markerClusterRenderer.setMinClusterSize(2);
        markerClusterRenderer.setAnimation(true);

        clusterManager.setRenderer(markerClusterRenderer);
        clusterManager.addItems(itemOnMap);
        clusterManager.cluster();
    }

    private List<PointOnMap> getItemOnMap() {
        List<PointOnMap> points = new ArrayList<>();

        for (FirebaseData item: point) {
            LatLng location = new LatLng(item.getLatitude(), item.getLongitude());
            points.add(new PointOnMap(item.getName(), location));
        }
        return points;
    }

    private void showPointLocation() {
        LatLng location = new LatLng(point.get(position).getLatitude(), point.get(position).getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));
    }

    private void showUserLocation() {
        try {
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            if (locationResult != null) {
                locationResult.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            showMarker(location.getLatitude(), location.getLongitude(),getString(R.string.marker_title_user_location));
                        } else {
                            showMarker(-33.8523341, 151.2106085,getString(R.string.marker_title_default_location));
                        }
                    }
                });
            }
        } catch (SecurityException securityException)  {
            Log.e(LOG_MESSAGE, Objects.requireNonNull(securityException.getMessage()) + securityException);
        }
    }

    private void showMarker(double latitude, double longitude, String markerTitle) {
        LatLng location = new LatLng(latitude, longitude);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.snowman_christmas);
        Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(location)
                .title(markerTitle)
                .icon(smallMarkerIcon);

        mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));
        mMap.addMarker(markerOptions).showInfoWindow();
    }

    private void showMissingPermissionError() {
        Permission.PermissionDeniedDialog.newInstance(true).show(Objects.requireNonNull(getFragmentManager()), "dialog");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (!Permission.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            mPermissionDenied = true;
        }
    }

    private void showMap(GoogleMap mMap) {
        MapSettings.defaultMapSettings(mMap);
        setUpClusterManager(mMap);
        mMap.setOnMapLongClickListener(this);

        if (position != -1) showPointLocation();
        else showUserLocation();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(activity != null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mPermissionDenied) {
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (writePresenter != null) writePresenter.onDestroy();
        if (dialogPresenter != null) dialogPresenter.onDestroy();
    }
}