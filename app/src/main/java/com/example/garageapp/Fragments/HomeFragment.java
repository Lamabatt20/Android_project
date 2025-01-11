package com.example.garageapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.garageapp.ServiceActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;
    private List<Customer> customerList;
    private EditText searchBar;
    private Button addServiceButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView);
        searchBar = view.findViewById(R.id.search_bar);
        addServiceButton = view.findViewById(R.id.add_service_button);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        customerList = new ArrayList<>();
        customerAdapter = new CustomerAdapter(customerList, position -> {
            // Handle item click to navigate to OrderDetail
            Customer clickedCustomer = customerList.get(position);
            Intent intent = new Intent(getActivity(), OrderDetail.class);
            intent.putExtra("customer_id", clickedCustomer.getId()); // Pass customer_id
            startActivity(intent);
        });
        recyclerView.setAdapter(customerAdapter);

        // Set up Add Service button
        addServiceButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ServiceActivity.class);
            startActivity(intent);
        });

        // Fetch customer data
        fetchCustomerData();

        // Set up search bar listener
        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterCustomers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        return view;
    }

    private void fetchCustomerData() {
        String url = "http://172.19.33.199/public_html/Android/dashboard.php";

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
                                customerList.add(new Customer(customerId, customerName, serviceName));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        customerAdapter.updateList(customerList);
                        Toast.makeText(getContext(), "Customers Loaded: " + customerList.size(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            Toast.makeText(getContext(), "Error fetching data: " + error.getMessage(), Toast.LENGTH_LONG).show();
        });

        queue.add(jsonArrayRequest);
    }

    private void filterCustomers(String query) {
        List<Customer> filteredList = new ArrayList<>();
        for (Customer customer : customerList) {
            if (customer.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(customer);
            }
        }
        customerAdapter.updateList(filteredList);
    }
}
