package com.example.garageapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class ServiceActivity extends AppCompatActivity {
    private Button btnSubmitService;
    private TextInputEditText edtServiceName, edtPrice, edtEstimatedTime;
    private ImageView back_button;

    private static final String PHP_URL = "http://10.0.2.2/public_html/Android/add_service.php";  // Replace with your PHP file URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        // Initialize views
        back_button = findViewById(R.id.back_button);
        TextInputLayout serviceNameLayout = findViewById(R.id.edtServiceName);
        TextInputLayout servicePriceLayout = findViewById(R.id.edtPrice);
        TextInputLayout serviceEstimatedTimeLayout = findViewById(R.id.edtEstimatedTime);

        // Access the inner TextInputEditText
        edtServiceName = (TextInputEditText) serviceNameLayout.getEditText();
        edtPrice = (TextInputEditText) servicePriceLayout.getEditText();
        edtEstimatedTime = (TextInputEditText) serviceEstimatedTimeLayout.getEditText(); serviceNameLayout.getEditText();

        btnSubmitService = findViewById(R.id.btnSubmitService);

        // Back button functionality
        back_button.setOnClickListener(v -> finish());

        // Submit service button functionality
        btnSubmitService.setOnClickListener(v -> {
            String serviceName = edtServiceName.getText().toString();
            String price = edtPrice.getText().toString();
            String estimatedTime = edtEstimatedTime.getText().toString();

            // Validate inputs
            if (serviceName.isEmpty() || price.isEmpty() || estimatedTime.isEmpty()) {
                Toast.makeText(ServiceActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Send data to PHP backend via Volley
                submitServiceToBackend(serviceName, price, estimatedTime);
                edtServiceName.setText("");
                edtPrice.setText("");
                edtEstimatedTime.setText("");
                //go back to the previous activity
                finish();
            }
        });



    }

    // Method to submit service data to PHP server using Volley
    private void submitServiceToBackend(String serviceName, String price, String estimatedTime) {
        // RequestQueue initialization
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // StringRequest to send POST request to PHP server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        Toast.makeText(ServiceActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(com.android.volley.VolleyError error) {
                        // Handle error
                        Toast.makeText(ServiceActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Map the data to be sent in the POST request
                Map<String, String> params = new HashMap<>();
                params.put("service_name", serviceName);
                params.put("price", price);
                params.put("estimated_time", estimatedTime);
                return params;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest);
    }
}
