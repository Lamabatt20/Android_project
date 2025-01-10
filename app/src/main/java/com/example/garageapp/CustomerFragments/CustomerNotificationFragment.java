package com.example.garageapp.CustomerFragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.garageapp.CustomerNotificationAdapter;
import com.example.garageapp.Order;
import com.example.garageapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerNotificationFragment extends Fragment {

    private RecyclerView notificationRecyclerView;
    private CustomerNotificationAdapter notificationAdapter;
    private List<Order> orders = new ArrayList<>();
    private RequestQueue requestQueue;
    private static final String BASE_URL = "http://192.168.1.108/public_html/Android/Customerphp/Notification.php";
    private int customerId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_customer_notification, container, false);

        // Initialize RecyclerView
        notificationRecyclerView = rootView.findViewById(R.id.reminders_list_recycler_view);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notificationAdapter = new CustomerNotificationAdapter(getContext(), orders);
        notificationRecyclerView.setAdapter(notificationAdapter);

        // Getting customer ID from the arguments
        if (getArguments() != null) {
            customerId = getArguments().getInt("customer_id", -1);
            if (customerId != -1) {
                // Load orders if valid customer ID
                if (orders.isEmpty()) {  // Only load if orders are empty
                    loadItems(rootView);
                } else {
                    updateUI(rootView);  // Skip loading if data is already available
                }
            } else {
                Toast.makeText(getContext(), "Invalid customer ID", Toast.LENGTH_SHORT).show();
            }
        }

        return rootView;
    }

    private void loadItems(View rootView) {
        // Clear the orders list before adding new data
        orders.clear();

        String url = BASE_URL + "?customer_id=" + customerId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Process response and add to orders list
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int orderId = object.getInt("order_id");
                                int serviceId = object.getInt("service_id");
                                int carId = object.getInt("car_id");
                                String serviceName = object.getString("service_name");
                                String carName = object.getString("car_name");
                                String state = object.getString("state");
                                String orderDate = object.getString("order_date");

                                Order order = new Order(orderId, serviceId, carId, state, serviceName, carName, orderDate);
                                orders.add(order);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Make sure updateUI is run on the main thread
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUI(rootView);  // Update RecyclerView after loading data
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("CustomerNotificationFragment", "Error: " + error.toString());
                        Toast.makeText(getContext(), "Failed to load orders", Toast.LENGTH_LONG).show();
                    }
                });

        // Add the request to the Volley queue
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private void updateUI(View rootView) {
        ImageView noDataImage = rootView.findViewById(R.id.no_data_image);
        RecyclerView recyclerView = rootView.findViewById(R.id.reminders_list_recycler_view);
        TextView text = rootView.findViewById(R.id.no_data_text);

        if (orders.isEmpty()) {
            noDataImage.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noDataImage.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            notificationAdapter.notifyDataSetChanged();


        }
    }
}
