package com.example.garageapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private final List<Car> carList;
    private final Context context;
    private final OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public CarAdapter(Context context, List<Car> carList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.carList = carList;
        this.onItemClickListener = onItemClickListener;
    }
    public CarAdapter(Context context, List<Car> carList) {
        this.context = context;
        this.carList = carList;
        onItemClickListener = null;
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        Car car = carList.get(position);

        Log.d("CarAdapter", "Binding car: " + car.getCars_name() + ", Photo: " + car.getPhoto());

        // Bind data to views
        Glide.with(context)
                .load(car.getPhoto())
                .into(holder.carImageView);

        holder.carNameTextView.setText(car.getCars_name());
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView carImageView;
        TextView carNameTextView;

        public CarViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            // Initialize views
            cardView = itemView.findViewById(R.id.cardView);
            carImageView = itemView.findViewById(R.id.car_image);
            carNameTextView = itemView.findViewById(R.id.car_name);

            // Set click listener on the CardView
            cardView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
        }
    }
}

