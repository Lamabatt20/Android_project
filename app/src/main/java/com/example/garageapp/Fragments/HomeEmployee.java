package com.example.garageapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.garageapp.Customer;
import com.example.garageapp.CustomerAdapter;
import com.example.garageapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeEmployee extends Fragment {

    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;
    private List<Customer> customerList;
    private List<Customer> filteredList; // For filtered customers
    private EditText searchBar;
    private String urlpath = "http://172.19.33.18"; // Adjust URL as needed

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employee_home, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView);
        searchBar = view.findViewById(R.id.search_bar);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        customerList = new ArrayList<>();
        filteredList = new ArrayList<>(); // Initialize filtered list
        customerAdapter = new CustomerAdapter(filteredList, position -> {
            // Handle item click to navigate to OrderDetail
            Customer clickedCustomer = filteredList.get(position); // Get customer from filtered list
            Intent intent = new Intent(getActivity(), OrderDetail.class);
            intent.putExtra("customer_id", clickedCustomer.getId()); // Pass customer_id
            intent.putExtra("order_id", clickedCustomer.getOrderId());
            intent.putExtra("service_id", clickedCustomer.getServiceId());
            intent.putExtra("state", clickedCustomer.getState());
            intent.putExtra("states_date", clickedCustomer.getStatesDate());
            intent.putExtra("order_date", clickedCustomer.getOrderDate());
            intent.putExtra("total_amount", clickedCustomer.getTotalAmount());
            startActivity(intent);
        });
        recyclerView.setAdapter(customerAdapter);

        // Fetch customer data
        fetchCustomerData();

        // Set up search bar listener
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterCustomers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return view;
    }

    private void fetchCustomerData() {
        String url = urlpath + "/public_html/Android/dashboard.php";

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        customerList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject order = response.getJSONObject(i);
                                int customerId = order.getInt("customer_id");
                                String customerName = order.getString("customer_name");
                                String serviceName = order.getString("service_name");
                                int orderId = order.getInt("order_id");
                                int serviceId = order.getInt("service_id");
                                String state = order.getString("state");
                                String statesDate = order.getString("states_date");
                                String orderDate = order.getString("order_date");
                                double totalAmount = order.getDouble("total_amount");

                                customerList.add(new Customer(customerId, customerName, serviceName, orderId, serviceId, state, statesDate, orderDate, totalAmount));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        filteredList.addAll(customerList); // Initially display all customers
                        customerAdapter.updateList(filteredList); // Update RecyclerView with initial data
                        Toast.makeText(getContext(), "Customers Loaded: " + customerList.size(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            Toast.makeText(getContext(), "Error fetching data: " + error.getMessage(), Toast.LENGTH_LONG).show();
        });

        queue.add(jsonArrayRequest);
    }

    private void filterCustomers(String query) {
        List<Customer> filtered = new ArrayList<>();
        for (Customer customer : customerList) {
            if (customer.getName().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(customer);
            }
        }
        filteredList.clear(); // Clear the previous filtered list
        filteredList.addAll(filtered); // Add the filtered customers to the list
        customerAdapter.updateList(filteredList); // Update the RecyclerView adapter
    }
}
