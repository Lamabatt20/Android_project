package com.example.garageapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.garageapp.R;

import org.json.JSONObject;

public class ReportFragment extends Fragment {

    private TextView txtNumberOfCustomers;
    private TextView txtNumberOfEmployees;
    private TextView txtRevenue;
    private TextView txtProfit;
    private TextView txtCustomerOfTheMonth;
    private TextView txtEmployeeOfTheMonth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);

        // Initialize the TextViews
        txtNumberOfCustomers = rootView.findViewById(R.id.txtNumberOfCustomers);
        txtNumberOfEmployees = rootView.findViewById(R.id.txtNumberOfEmployees);
        txtRevenue = rootView.findViewById(R.id.txtRevenue);
        txtProfit = rootView.findViewById(R.id.txtProfit);
        txtCustomerOfTheMonth = rootView.findViewById(R.id.txtCustomerOfTheMonth);
        txtEmployeeOfTheMonth = rootView.findViewById(R.id.txtEmployeeOfTheMonth);

        // Fetch data using Volley
        fetchData();

        return rootView;
    }

    private void fetchData() {
        String url = "http://192.168.1.108/public_html/Android/report.php";

        // Initialize Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        // Create a JSON object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse employee details
                            JSONObject topEmployee = response.getJSONObject("top_employee");
                            String employeeName = topEmployee.getString("employee_name");
                            String employeePhotoUrl = topEmployee.getString("photo");
                            int employeeOrders = topEmployee.getInt("total_orders");

                            // Parse customer details
                            JSONObject topCustomer = response.getJSONObject("top_customer");
                            String customerName = topCustomer.getString("customer_name");
                            String customerPhotoUrl = topCustomer.getString("photo");
                            int customerOrders = topCustomer.getInt("total_orders");
                            String fullImageUrl = "http://192.168.1.108" + employeePhotoUrl;
                            String fullImageUrl2 = "http://192.168.1.108" + customerPhotoUrl;

                            // Parse general details
                            int totalCustomers = response.getInt("total_customers");
                            int totalEmployees = response.getInt("total_employees");
                            double revenue = response.getDouble("total_revenue");
                            double profit = response.getDouble("profit");

                            // Set data for Employee of the Month
                            txtEmployeeOfTheMonth.setText("Employee of the month "+employeeName + " (" + employeeOrders + " orders)");
                            loadDrawableTop(txtEmployeeOfTheMonth, fullImageUrl);

                            // Set data for Customer of the Month
                            txtCustomerOfTheMonth.setText("Customer of the month "+customerName + " (" + customerOrders + " orders)");
                            loadDrawableTop(txtCustomerOfTheMonth, fullImageUrl2);

                            // Set the values in the TextViews
                            txtNumberOfCustomers.append(" " + totalCustomers);
                            txtNumberOfEmployees.append(" " + totalEmployees);
                            txtRevenue.append(" $" + revenue);
                            txtProfit.append(" $" + profit);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed to parse data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(getActivity(), "Failed to load data", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        );

        // Add the request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    private void loadDrawableTop(TextView textView, String imageUrl) {
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .circleCrop()
                .override(450, 450)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        // Convert bitmap to Drawable
                        Drawable drawable = new BitmapDrawable(getResources(), resource);
                        drawable.setBounds(0, 0, 450, 450); // Match the size

                        // Set drawableTop
                        textView.setCompoundDrawables(null, drawable, null, null);
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        // Handle if the load is cleared
                    }
                });


    }
}
