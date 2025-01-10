package com.example.garageapp.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.garageapp.NotificationAdapter;
import com.example.garageapp.NotificationItem;
import com.example.garageapp.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotficationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationItem> notificationList;
    private TextView emptyMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notfication, container, false);
        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyMessage = view.findViewById(R.id.no_notification);

        // Initialize Notification List and Adapter
        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(requireContext(), notificationList);
        recyclerView.setAdapter(adapter);

        // Fetch notifications
        fetchNotifications();

        return view;
    }

    private void fetchNotifications() {
        String url = "http://192.168.1.108/public_html/Android/notification.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            int orderId = obj.getInt("order_id");
                            String orderDate = obj.getString("order_date");
                            String description = obj.getString("description");
                            String state = obj.getString("state");
                            int carId = obj.getInt("car_id");
                            String model = obj.getString("model");
                            String carName = obj.getString("cars_name");

                            NotificationItem item = new NotificationItem(orderId, orderDate, description, state, carId, model,carName);
                            notificationList.add(item);
                            
                            
                        }    
                        if (notificationList.isEmpty()) {
                            // Handle empty notification list, e.g., show a message or hide RecyclerView
                            recyclerView.setVisibility(View.GONE);
                            // Optionally, show a TextView with a message
                            emptyMessage.setVisibility(View.VISIBLE);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                   
                    } catch (Exception e) {
                        Log.e("NotificationFragment", "Error parsing notifications", e);
                    }
                },
                error -> Log.e("NotificationFragment", "Error fetching notifications: " + error.getMessage())
        );

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }
}
