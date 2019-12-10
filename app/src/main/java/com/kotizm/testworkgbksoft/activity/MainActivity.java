package com.kotizm.testworkgbksoft.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kotizm.testworkgbksoft.R;
import com.kotizm.testworkgbksoft.firebase.database.receive.presenter.IFirebaseReceivePresenter;
import com.kotizm.testworkgbksoft.firebase.database.receive.data.FirebaseReceiveData;
import com.kotizm.testworkgbksoft.firebase.database.receive.presenter.FirebaseReceivePresenter;
import com.kotizm.testworkgbksoft.firebase.database.receive.view.IFirebaseReceiveView;
import com.kotizm.testworkgbksoft.model.FirebaseData;
import com.kotizm.testworkgbksoft.utils.dialog.progress.IMyProgressDialog;
import com.kotizm.testworkgbksoft.utils.dialog.progress.MyProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.parceler.Parcels;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IFirebaseReceiveView {

    private BottomNavigationView navView;
    private AppBarConfiguration appBarConfiguration;

    private IMyProgressDialog iProgressDialog;
    private IFirebaseReceivePresenter receivePresenter;

    private static final String LOG_MESSAGE = "log_message";
    private static final String POINT = "point_from_firebase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_point, R.id.navigation_map, R.id.navigation_profile)
                .build();

        iProgressDialog = new MyProgressDialog(this);
        iProgressDialog.showProgress();

        receivePresenter = new FirebaseReceivePresenter(this, new FirebaseReceiveData());
        receivePresenter.requestData();
    }

    @Override
    public void onResponseSuccess(List<FirebaseData> dataFirebase) {
        if (iProgressDialog != null) {
            iProgressDialog.hideProgress();
            iProgressDialog = null;
        }
        if (receivePresenter != null) receivePresenter.onDestroy();

        Bundle bundle = new Bundle();
        bundle.putParcelable(POINT, Parcels.wrap(dataFirebase));
        initNavController(bundle);
    }

    private void initNavController(final Bundle bundle) {
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.navigation_point) {
                    navController.navigate(R.id.navigation_point, bundle);
                }
                if (menuItem.getItemId() == R.id.navigation_map) {
                    navController.navigate(R.id.navigation_map, bundle);
                }
                if (menuItem.getItemId() == R.id.navigation_profile) {
                    navController.navigate(R.id.navigation_profile);
                }
                return false;
            }
        });
        navController.navigate(R.id.navigation_point, bundle);
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        if (iProgressDialog != null) iProgressDialog.hideProgress();

        Toast.makeText(this, getString(R.string.firebase_database_error) + throwable, Toast.LENGTH_LONG).show();
        Log.e(LOG_MESSAGE, getString(R.string.firebase_database_error) + throwable);
    }
}