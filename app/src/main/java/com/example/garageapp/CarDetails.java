package com.example.garageapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CarDetails extends AppCompatActivity {
    public static LinearLayout upcoming_layout, upcoming;
    public static TextView toolbar_title;
    private TextView carModel, carEngine, carYear, carOdometer, carTransmission, history;
    private ImageView carImage;
    public static String carID, carName;
    Button selectNewServiceButton;
    private String pathurl= "http://172.19.33.18";

    private static final int REQUEST_CODE = 1; // Define the request code for permission

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        toolbar_title = findViewById(R.id.toolbar_title);
        carImage = findViewById(R.id.car_image);
        carModel = findViewById(R.id.car_Model);
        carEngine = findViewById(R.id.car_Engine);
        carYear = findViewById(R.id.car_Year);
        carOdometer = findViewById(R.id.car_Odometer);
        carTransmission = findViewById(R.id.car_Transmission);
        history = findViewById(R.id.service_history);
        upcoming = findViewById(R.id.upcoming_service);
        selectNewServiceButton = findViewById(R.id.select_new_service_button);
        upcoming_layout = findViewById(R.id.upcoming_layout);

        carID = getIntent().getStringExtra("car_id");
        Log.d("======***=========", carID + "");
        if (carID == null || carID.isEmpty()) {
            Toast.makeText(this, "Car ID is missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Check for storage permission
        checkStoragePermission();

        fetchCarData(carID);

        selectNewServiceButton.setOnClickListener(view -> {
            Intent intent = new Intent(CarDetails.this, SelectServiceActivity.class);
            intent.putExtra("carName", carName);
            startActivity(intent);
        });
    }

    // Check if the app has storage permission
    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can access storage
            } else {
                // Permission denied, notify the user
                Toast.makeText(this, "Storage permission is required to load images", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchCarData(String carNumber) {
        String url = pathurl+"/public_html/Android/Customerphp/get_car.php?car_number=" + carNumber;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.has("error")) {
                            Toast.makeText(CarDetails.this, "Car not found!", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }

                        carName = response.getString("cars_name");
                        toolbar_title.setText(carName);
                        carModel.setText(response.getString("model"));
                        String carPhotoUrl = getIntent().getStringExtra("car_photo");
                        carEngine.setText(response.getString("engine_specification"));
                        carYear.setText(response.getString("year"));
                        carOdometer.setText(response.getString("odometer"));
                        carTransmission.setText(response.getString("transmission"));

                        // Load car image using Glide after checking permission
                        Glide.with(CarDetails.this)
                                .load(carPhotoUrl)
                                .into(carImage);

                        JSONArray historyArray = response.getJSONArray("history");
                        for (int i = 0; i < historyArray.length(); i++) {
                            JSONObject historyItem = historyArray.getJSONObject(i);
                            history.append("- " + historyItem.getString("notes") +
                                    "   Done with date " + historyItem.getString("date") + "\n");
                        }

                        JSONArray servicesArray = response.getJSONArray("services");
                        for (int i = 0; i < servicesArray.length(); i++) {
                            JSONObject serviceItem = servicesArray.getJSONObject(i);
                            String note = "- " + serviceItem.getString("service_name") +
                                    " need  (" + serviceItem.getString("estimated_time") + ")\n";
                            String orderID = serviceItem.getString("order_id");
                            addServiceButton(note, orderID);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CarDetails.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(CarDetails.this, "Error fetching car data", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }

    public void addServiceButton(String note, String orderID) {
        ImageButton serviceButton = new ImageButton(this);
        serviceButton.setImageResource(R.drawable.del);
        serviceButton.setBackgroundColor(Color.TRANSPARENT);
        TextView textView = new TextView(this);
        textView.setText(note);

        upcoming.addView(textView);
        upcoming_layout.addView(serviceButton);

        serviceButton.setOnClickListener(v -> {

            new AlertDialog.Builder(CarDetails.this)
                    .setTitle("Delete Service")
                    .setMessage("Are you sure you want to delete this Service?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            upcoming_layout.removeView(serviceButton);
                            upcoming.removeView(textView);
                            deleteServiceFromDatabase(orderID);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .show();

        });
    }

    private void deleteServiceFromDatabase(String orderID) {
        String url = pathurl+"/public_html/Android/Customerphp/delete_service.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("order_id", orderID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(CarDetails.this, "Service deleted successfully.", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = response.getString("message");
                            Toast.makeText(CarDetails.this, "Failed: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CarDetails.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(CarDetails.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }
}
