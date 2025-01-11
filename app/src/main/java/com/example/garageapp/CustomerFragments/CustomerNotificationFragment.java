package com.example.garageapp.CustomerFragments;

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
    private static final String BASE_URL = "http://10.0.2.2/public_html/Android/Cutomerphp/Notification.php";
    private int customerId;

    public CustomerNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_customer_notification, container, false);

        // Getting customer ID from the arguments
        if (getArguments() != null) {
            customerId = getArguments().getInt("customer_id", -1);
        }

        if (customerId == -1) {
            Toast.makeText(getContext(), "Invalid customer ID", Toast.LENGTH_SHORT).show();
            return rootView;
        }

        // Initialize RecyclerView
        notificationRecyclerView = rootView.findViewById(R.id.reminders_list_recycler_view);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize order list and RequestQueue
        requestQueue = Volley.newRequestQueue(getContext());

        // Load orders
        loadItems(rootView);

        return rootView;
    }

    private void loadItems(View rootView) {
        String url = BASE_URL + "?customer_id=" + customerId;

        // Create a StringRequest to make a GET request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
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
                        updateUI(rootView);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log the error response
                        Log.e("CustomerNotificationFragment", "Error: " + error.toString());
                        Toast.makeText(getContext(), "Failed to load orders", Toast.LENGTH_LONG).show();
                    }
                });

        // Add the request to the RequestQueue
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

            if (notificationAdapter == null) {
                notificationAdapter = new CustomerNotificationAdapter(getContext(), orders);
                recyclerView.setAdapter(notificationAdapter);
            } else {
                notificationAdapter.notifyDataSetChanged();
            }
        }
    }
}
