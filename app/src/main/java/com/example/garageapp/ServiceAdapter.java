package com.example.garageapp;


import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private Context context;
    private List<Service> items;
    public ServiceAdapter(Context context, List<Service> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_captioned_image,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Service service = items.get(position);
        CardView cardView = holder.cardView;

        ImageView imageView = cardView.findViewById(R.id.image);
        Glide.with(context).load(service.getImage()).into(imageView);
        TextView nameTextView = cardView.findViewById(R.id.txtName);
        TextView priceTextView = cardView.findViewById(R.id.txtPrice);
        TextView timeTextView = cardView.findViewById(R.id.txtEstimatedTime);
        nameTextView.setText(service.getName());
        priceTextView.setText(service.getPrice());
        timeTextView.setText(service.getEstimatedTime());


        cardView.setOnClickListener(v -> {
            // Handle card click
            Intent intent = new Intent(context, DetailsServiceActivity.class);
            intent.putExtra("service_name", service.getName());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}