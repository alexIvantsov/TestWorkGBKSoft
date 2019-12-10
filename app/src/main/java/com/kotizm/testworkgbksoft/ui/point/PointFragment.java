package com.kotizm.testworkgbksoft.ui.point;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kotizm.testworkgbksoft.R;
import com.kotizm.testworkgbksoft.adapter.MenuAdapterClick;
import com.kotizm.testworkgbksoft.firebase.database.delete.data.FirebaseDeleteData;
import com.kotizm.testworkgbksoft.firebase.database.delete.presenter.FirebaseDeletePresenter;
import com.kotizm.testworkgbksoft.firebase.database.delete.presenter.IFirebaseDeletePresenter;
import com.kotizm.testworkgbksoft.firebase.database.delete.view.IFirebaseDeleteView;
import com.kotizm.testworkgbksoft.model.FirebaseData;

import org.parceler.Parcels;

import java.util.List;
import java.util.Objects;

public class PointFragment extends Fragment
        implements MenuAdapterClick.OnItemClickListener, MenuAdapterClick.OnItemLongClickListener,
        IFirebaseDeleteView {

    private View root;
    private View layoutNoData;

    private boolean isLongClick;
    private RecyclerView recyclerView;
    private AppCompatActivity activity;

    private List<FirebaseData> dataFirebase;
    private IFirebaseDeletePresenter databasePresenter;

    private static final String POINT_POSITION = "point_position";
    private static final String LOG_MESSAGE = "log_message";
    private static final String POINT = "point_from_firebase";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        activity = (AppCompatActivity) Objects.requireNonNull(getActivity());
        FragmentActivity fragment = Objects.requireNonNull(getActivity());

        layoutNoData = root.findViewById(R.id.recyclerNoData);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragment));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                activity.finish();
            }
        });
        return root;
    }

    private void checkRecyclerData() {
        if (layoutNoData != null) {
            if (dataFirebase != null && dataFirebase.size() > 0) {
                layoutNoData.setVisibility(View.INVISIBLE);
            } else {
                layoutNoData.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        if (!isLongClick) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(POINT, Parcels.wrap(dataFirebase));
            bundle.putInt(POINT_POSITION, position);
            Navigation.findNavController(root).navigate(R.id.navigation_map, bundle);
        } else isLongClick = false;
    }

    @Override
    public void onItemLongClick(int position) {
        isLongClick = true;
        if (databasePresenter == null) {
            databasePresenter = new FirebaseDeletePresenter(this, new FirebaseDeleteData());
        }
        databasePresenter.deleteItem(dataFirebase.get(position));
        dataFirebase.remove(position);

        Objects.requireNonNull(recyclerView.getAdapter()).notifyItemRemoved(position);
        Objects.requireNonNull(recyclerView.getAdapter()).notifyItemRangeChanged(position, dataFirebase.size());
        checkRecyclerData();
    }

    @Override
    public void onDeleteSuccess(FirebaseData item) {
        if (item != null) {
            Toast.makeText(activity, activity.getString(R.string.firebase_database_delete_item) + item.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteFailure(Throwable throwable) {
        Toast.makeText(activity, activity.getString(R.string.firebase_database_error) + throwable, Toast.LENGTH_LONG).show();
        Log.e(LOG_MESSAGE, getString(R.string.firebase_database_error) + throwable);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            dataFirebase = Parcels.unwrap(getArguments().getParcelable(POINT));
            MenuAdapterClick adapter = new MenuAdapterClick(dataFirebase, getLayoutInflater(), this, this);
            recyclerView.setAdapter(adapter);
            checkRecyclerData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (databasePresenter != null) databasePresenter.onDestroy();
        if (layoutNoData != null) layoutNoData.setVisibility(View.INVISIBLE);
    }
}