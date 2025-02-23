package com.example.garageapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<NotificationItem> notificationList;
    private Context context;
    private String pathurl= "http://172.19.33.18";

    public NotificationAdapter(Context context, List<NotificationItem> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationItem item = notificationList.get(position);
        holder.tvDescription.setText(item.getDescription());
        holder.tvCarModel.setText("Car: " + item.getModel());
        holder.tvCarName.setText("Car Name: " + item.getCarName());
        holder.tvOrderDate.setText("Order Date: " + item.getOrderDate());

        // Initialize CheckBox state
        holder.cbComplete.setOnCheckedChangeListener(null); // Avoid unwanted triggers during recycling
        holder.cbComplete.setChecked(item.getState().equalsIgnoreCase("Done"));

        // Set listener for CheckBox
        holder.cbComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                markOrderAsComplete(item.getOrderId(), holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    private void markOrderAsComplete(int orderId, NotificationViewHolder holder) {
        String url = pathurl+"/public_html/Android/updateOrder.php";

        // Retrieve employee_id from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("RepairApp", Context.MODE_PRIVATE);
        int employeeId = sharedPreferences.getInt("employee_id", -1);

        if (employeeId == -1) {
            Toast.makeText(context, "Error: Employee not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(context);

        // Create a JSONObject for the request body
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("order_id", orderId);
            requestBody.put("user_id", employeeId); // Include employee_id
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error creating request data!", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    // Handle server response as plain text
                    if (response.trim().equalsIgnoreCase("success")) {
                        Toast.makeText(context, "Order moved to history!", Toast.LENGTH_SHORT).show();
                        holder.cbComplete.setEnabled(false);
                    } else {
                        Toast.makeText(context, "Failed to update order: " + response, Toast.LENGTH_SHORT).show();
                        Log.d("Debug", "URL: " + url);
                        Log.d("Debug", "Payload: " + requestBody.toString());
                        holder.cbComplete.setChecked(false);
                    }
                },
                error -> {
                    Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    holder.cbComplete.setChecked(false);
                    Log.e("Debug", "Error: ", error);
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return requestBody.toString().getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(stringRequest);
    }


    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvCarModel, tvOrderDate, tvCarName;
        CheckBox cbComplete;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCarModel = itemView.findViewById(R.id.tvCarModel);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            cbComplete = itemView.findViewById(R.id.cbComplete); // Initialize CheckBox
        }
    }

}
