package com.example.garageapp.CustomerFragments;

import android.content.Context;
import android.content.SharedPreferences;
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
    private static final String PREF_NAME = "GarageAppPreferences";

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
        // First, try to load orders from SharedPreferences
        List<Order> savedOrders = getOrdersFromSharedPreferences(getContext());
        if (savedOrders != null && !savedOrders.isEmpty()) {
            orders.clear();
            orders.addAll(savedOrders);
            updateUI(rootView);
        } else {
            // If no saved orders, load them from the server
            String url = BASE_URL + "?customer_id=" + customerId;

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
                            // Save the orders to SharedPreferences after loading from the server
                            saveOrdersToSharedPreferences(getContext(), orders);
                            updateUI(rootView);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("CustomerNotificationFragment", "Error: " + error.toString());
                            Toast.makeText(getContext(), "Failed to load orders", Toast.LENGTH_LONG).show();
                        }
                    });

            requestQueue.add(stringRequest);
        }
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

    // Save orders to SharedPreferences
    private void saveOrdersToSharedPreferences(Context context, List<Order> orders) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            JSONArray jsonArray = new JSONArray();
            for (Order order : orders) {
                JSONObject orderObject = new JSONObject();
                orderObject.put("order_id", order.getOrderId());
                orderObject.put("service_id", order.getServiceId());
                orderObject.put("car_id", order.getCarId());
                orderObject.put("service_name", order.getServiceName());
                orderObject.put("car_name", order.getCarName());
                orderObject.put("state", order.getState());
                orderObject.put("order_date", order.getOrderDate());
                jsonArray.put(orderObject);
            }
            editor.putString("orders_data", jsonArray.toString());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get orders from SharedPreferences
    private List<Order> getOrdersFromSharedPreferences(Context context) {
        List<Order> orders = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String ordersJson = sharedPreferences.getString("orders_data", "[]");

        try {
            JSONArray jsonArray = new JSONArray(ordersJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject orderObject = jsonArray.getJSONObject(i);
                int orderId = orderObject.getInt("order_id");
                int serviceId = orderObject.getInt("service_id");
                int carId = orderObject.getInt("car_id");
                String serviceName = orderObject.getString("service_name");
                String carName = orderObject.getString("car_name");
                String state = orderObject.getString("state");
                String orderDate = orderObject.getString("order_date");

                Order order = new Order(orderId, serviceId, carId, state, serviceName, carName, orderDate);
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }
}
