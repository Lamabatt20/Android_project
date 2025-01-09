package com.example.garageapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerNotificationAdapter extends RecyclerView.Adapter<CustomerNotificationAdapter.NotificationViewHolder> {

    private List<Order> orders;
    private Context context;

    // Constructor
    public CustomerNotificationAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }


    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notify, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder( NotificationViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.reminderInfo.setText(order.getServiceName() != null ? order.getServiceName() : "Unknown Service");
        holder.carName.setText((order.getCarName() != null ? order.getCarName() : "Unknown Car"));
        holder.dateToRemind.setText( order.getOrderDate());

        int progress = calculateProgress(order.getState());
        holder.progressBar.setProgress(progress);
        holder.progressText.setText(progress + "%");
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView reminderInfo;
        TextView carName;
        TextView dateToRemind;
        ProgressBar progressBar;
        TextView progressText;
        CardView cardView;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            reminderInfo = itemView.findViewById(R.id.reminder_info);
            carName = itemView.findViewById(R.id.NameCar);
            dateToRemind = itemView.findViewById(R.id.date_to_remind);
            progressBar = itemView.findViewById(R.id.progress_circle);
            progressText = itemView.findViewById(R.id.progress_text);
            cardView=itemView.findViewById(R.id.notify_card);
        }
    }

    private int calculateProgress(String state) {
        if ("Done".equals(state)) {
            return 100;
        } else if ("Yet".equals(state)) {
            return 50;
        }
        return 0;
    }
}