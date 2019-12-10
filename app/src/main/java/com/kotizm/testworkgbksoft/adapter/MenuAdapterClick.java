package com.kotizm.testworkgbksoft.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.kotizm.testworkgbksoft.model.FirebaseData;

import java.util.List;

public class MenuAdapterClick extends MenuAdapter implements View.OnClickListener, View.OnLongClickListener {

    private final OnItemClickListener itemClickListener;
    private final OnItemLongClickListener itemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public MenuAdapterClick(List<FirebaseData> dataFirebase, LayoutInflater inflater, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
        super(dataFirebase, inflater);

        itemClickListener = clickListener;
        itemLongClickListener = longClickListener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MenuViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setOnLongClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        holder.itemView.setTag(position);
    }

    @Override
    public void onClick(View view) {
        Integer position = (Integer)view.getTag();
        itemClickListener.onItemClick(position);
    }

    @Override
    public boolean onLongClick(View view) {
        Integer position = (Integer)view.getTag();
        itemLongClickListener.onItemLongClick(position);
        return false;
    }
}