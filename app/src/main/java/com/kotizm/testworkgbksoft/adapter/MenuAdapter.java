package com.kotizm.testworkgbksoft.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kotizm.testworkgbksoft.R;
import com.kotizm.testworkgbksoft.model.FirebaseData;

import java.lang.ref.WeakReference;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {

    private List<FirebaseData> dataFirebase;
    private final WeakReference<LayoutInflater> mInflater;

    MenuAdapter(List<FirebaseData> dataFirebase, LayoutInflater inflater) {
        mInflater = new WeakReference<>(inflater);
        this.dataFirebase = dataFirebase;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = mInflater.get();

        if (inflater != null) {
            return new MenuViewHolder(inflater.inflate(R.layout.fragment_point, parent, false));
        } else throw new RuntimeException(parent.getContext().getResources().getString(R.string.runtime_exception_view_holder));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MenuViewHolder viewHolder, int position) {
        viewHolder.nameTextView.setText(dataFirebase.get(position).getName());
        viewHolder.latitudeTextView.setText(String.valueOf(dataFirebase.get(position).getLatitude()));
        viewHolder.longitudeTextView.setText(String.valueOf(dataFirebase.get(position).getLongitude()));
    }

    @Override
    public int getItemCount() {
        return dataFirebase == null ? 0 : dataFirebase.size();
    }
}

class MenuViewHolder extends RecyclerView.ViewHolder {

    TextView nameTextView;
    TextView latitudeTextView;
    TextView longitudeTextView;

    MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.item_menu_name);
        latitudeTextView = itemView.findViewById(R.id.item_menu_latitude);
        longitudeTextView = itemView.findViewById(R.id.item_menu_longitude);
    }
}