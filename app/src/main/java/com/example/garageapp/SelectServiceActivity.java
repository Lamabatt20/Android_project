package com.example.garageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectServiceActivity extends AppCompatActivity {
    private List<Service> items = new ArrayList<>();
    private RecyclerView recycler;
    private TextView tvTitle;
    private TextView toolbar_title;
    private static final String BASE_URL = "http://192.168.1.108/public_html/Android/Customerphp/get_menu.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);
        toolbar_title = findViewById(R.id.toolbar_title);
        tvTitle = findViewById(R.id.tvTitle);
        recycler = findViewById(R.id.service_recycler); // Update RecyclerView ID
        toolbar_title.setText("Service List");

        recycler.setLayoutManager(new LinearLayoutManager(this));
        tvTitle.setText(getIntent().getStringExtra("service_name"));

        loadItems();
    }

    private void loadItems() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                String name = object.getString("service_name");
                                String price = object.getString("price");
                                String estimatedTime = object.getString("estimated_time");
                                String image = "your_default_image_url"; // Default image URL

                                Service service = new Service(name, price, estimatedTime, image);
                                items.add(service);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ServiceAdapter adapter = new ServiceAdapter(SelectServiceActivity.this, items);
                        recycler.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelectServiceActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(SelectServiceActivity.this).add(stringRequest);
    }

}