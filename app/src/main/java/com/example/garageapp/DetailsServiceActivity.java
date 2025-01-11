package com.example.garageapp;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class DetailsServiceActivity extends AppCompatActivity {
    private static LinearLayout upcoming_layout;
    private static LinearLayout upcoming;

    private TextView toolbar_title, estimated_time, price, description, DateTextView;
    private Button add_service_button;
    private ImageButton DateButton;
    private static String carID, serviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_service);

        upcoming = CarDetails.upcoming;
        upcoming_layout = CarDetails.upcoming_layout;

        toolbar_title = findViewById(R.id.toolbar_title);
        estimated_time = findViewById(R.id.estimated_time);
        price = findViewById(R.id.price);
        DateTextView = findViewById(R.id.DateTextView);
        DateButton = findViewById(R.id.DateButton);
        description = findViewById(R.id.description);
        add_service_button = findViewById(R.id.add_service_button);


        toolbar_title.setText(CarDetails.toolbar_title.getText());
        carID = CarDetails.carID;
        serviceName = getIntent().getStringExtra("service_name");

        if (serviceName != null) {
            toolbar_title.setText(serviceName);
            fetchServiceDetails(serviceName);
        }
        setTodayDate();
        DateButton.setOnClickListener(view -> {
            calender();
        });

        add_service_button.setOnClickListener(view -> {
            String date = DateTextView.getText().toString();
            addServiceToDatabase(date, description.getText().toString(), carID, price.getText().toString(), estimated_time.getText().toString(),serviceName);

        });
    }

    private void fetchServiceDetails(String serviceName) {
        String url = "http://10.0.2.2/public_html/Android/Customerphp/get_service_details.php?service_name=" + serviceName;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        estimated_time.setText(response.getString("estimated_time"));
                        price.setText( response.getString("price"));
                        String note=("- " + serviceName +
                                " need  (" + response.getString("estimated_time") + ")\n");
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }}, error -> {});
        queue.add(jsonObjectRequest);
    }

    private void addServiceToDatabase(String Tarekh, String note, String carID, String price, String estimatedTime, String serviceName) {
        String url = "http://10.0.2.2/public_html/Android/Customerphp/add_service.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("date", formatte(Tarekh));
            jsonParams.put("description", note);
            jsonParams.put("carID", carID);
            jsonParams.put("price", price);//
            jsonParams.put("service_name", serviceName);
            jsonParams.put("estimated_time", estimatedTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                response -> {
                    try {
                        String result = response.getString("result");
                        if (result.equals("success")) {
                            String orderID = response.getString("order_id"); // الحصول على order_id الجديد
                            Toast.makeText(DetailsServiceActivity.this, "Order added successfully. Order ID: " + orderID, Toast.LENGTH_SHORT).show();
                            Log.d("OrderID", "New Order ID: " + orderID);
                            String noteee=("- " +serviceName +
                                    " need  (" + estimatedTime+ ")\n");
                            addServiceButton(noteee,orderID);

                        } else {
                            String message = response.getString("message");
                            Toast.makeText(DetailsServiceActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(DetailsServiceActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(DetailsServiceActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(request);
    }


    public String formatte(String Tarekh) {

        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = inputFormat.parse(Tarekh);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String outputDate = outputFormat.format(date);
        return outputDate;
    }
    public void calender() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                DetailsServiceActivity.this,
                (DatePicker view1, int selectedYear, int selectedMonth, int selectedDay) -> {
                    String selectedDate = selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear;
                    DateTextView.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }
    private void setTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String todayDate = day + "-" + month + "-" + year;
        DateTextView.setText(todayDate);
    }

    public  void addServiceButton(String note, String order_id) {
        ImageButton serviceButton = new ImageButton(this);
        serviceButton.setImageResource(R.drawable.del);
        serviceButton.setBackgroundColor(Color.TRANSPARENT);
        TextView TextView = new TextView(this);
        TextView.setText(note);
        upcoming.addView(TextView);
        upcoming_layout.addView(serviceButton);

        serviceButton.setOnClickListener(v -> {
            upcoming_layout.removeView(serviceButton);
            upcoming.removeView(TextView);
            //delete serves frome data base
            deleteServiceFromDatabase(order_id);
        });

    }
    private void deleteServiceFromDatabase(String order_id) {
        String url = "http://10.0.2.2/public_html/Android/Customerphp/delete_service.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("order_id", order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Log.d("DeleteService", "Service deleted successfully.");
                            Toast.makeText(DetailsServiceActivity.this, "Service deleted successfully.", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = response.getString("message");
                            Log.d("DeleteService", "Failed to delete service: " + message);
                            Toast.makeText(DetailsServiceActivity.this, "Failed: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(DetailsServiceActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.e("DeleteService", "Error deleting service: " + error.getMessage());
                    Toast.makeText(DetailsServiceActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }


}
