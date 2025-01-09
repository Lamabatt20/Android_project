package com.example.garageapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.garageapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class OrderDetail extends AppCompatActivity {
    private TextView customerNameTextView;
    private TextView phoneNumberTextView;
    private TextView orderIdTextView;
    private TextView orderDateTextView;
    private TextView totalAmountTextView;
    private TextView stateTextView;
    private TextView serviceIdTextView;
    private TextView employeeid;
    private int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail);

        // Initialize the TextViews from XML layout
        customerNameTextView = findViewById(R.id.customerNameTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        orderIdTextView = findViewById(R.id.orderIdTextView);
        orderDateTextView = findViewById(R.id.orderDateTextView);
        totalAmountTextView = findViewById(R.id.totalAmount);
        stateTextView = findViewById(R.id.stateTextView);
        serviceIdTextView = findViewById(R.id.serviceIdTextView);
        employeeid = findViewById(R.id.employeeid);

        // Retrieve the customer ID passed via intent
        customerId = getIntent().getIntExtra("customer_id", -1);

        if (customerId != -1) {
            fetchOrderDetails(customerId);
        } else {
            Toast.makeText(this, "Invalid customer ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchOrderDetails(int customerId) {
        // URL of the PHP endpoint that returns order details for the given customer ID
        String url = "http://172.19.33.199/public_html/Android/detaileddashboard.php?customer_id=" + customerId;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create a JsonArrayRequest (since your PHP returns a JSON array)
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Log the full response to help debug
                        Log.d("OrderDetail", "Response: " + response.toString());

                        try {
                            if (response.length() > 0) {
                                // Assuming the order is returned as an array, extract the first element
                                JSONObject order = response.getJSONObject(0);

                                // Extract data from the response
                                String customerName = order.getString("customer_name");
                                String phoneNumber = order.getString("phone_number");
                                int orderId = order.getInt("order_id");
                                String orderDate = order.getString("order_date");
                                double totalAmount = order.getDouble("total_amount");
                                String state = order.getString("state");
                                int serviceId = order.getInt("service_id");
                                String statesDate = order.getString("states_date");

                                // Update the UI with the order details
                                customerNameTextView.setText("Customer Name: " + customerName);
                                phoneNumberTextView.setText("Phone: " + phoneNumber);
                                orderIdTextView.setText("Order ID: " + orderId);
                                orderDateTextView.setText("Order Date: " + orderDate);
                                totalAmountTextView.setText("Total Amount: $" + totalAmount);
                                stateTextView.setText("State: " + state);
                                serviceIdTextView.setText("Service ID: " + serviceId);
                                employeeid.setText("Start Date: " + statesDate);
                            } else {
                                Toast.makeText(OrderDetail.this, "No orders found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("OrderDetail", "Error parsing data: " + e.getMessage());
                            e.printStackTrace();
                            Toast.makeText(OrderDetail.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.VolleyError error) {
                Toast.makeText(OrderDetail.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }
}
