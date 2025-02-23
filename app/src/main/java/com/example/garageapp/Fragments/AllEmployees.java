package com.example.garageapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.garageapp.Employee;
import com.example.garageapp.EmployeeAdapter;
import com.example.garageapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllEmployees extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmployeeAdapter adapter;
    private List<Employee> employeeList;
    private String pathurl = "http://172.19.33.18";
    ImageView backButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_employees);

        backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> {
            finish();
        });


        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerViewEmployee);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        employeeList = new ArrayList<>();
        adapter = new EmployeeAdapter(this, employeeList);
        recyclerView.setAdapter(adapter);

        // Fetch employee data
        fetchEmployees();
    }
    private void fetchEmployees() {
        String url = pathurl + "/public_html/Android/all_employees.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                Log.d("AllEmployees", "Raw JSON Object: " + obj.toString());

                                String employeeIdStr = obj.getString("employee_id");
                                Log.d("AllEmployees", "Raw employee_id: " + employeeIdStr);

                                int employeeId = Integer.parseInt(employeeIdStr);
                                Log.d("AllEmployees", "Parsed Employee ID: " + employeeId);

                                String employeeName = obj.getString("employee_name");
                                Employee employee = new Employee(employeeId, employeeName);

                                Log.d("AllEmployees", "Employee ID: " + employee.getEmployeeId() + ", Employee Name: " + employee.getEmployeeName());
                                employeeList.add(employee);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AllEmployees.this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AllEmployees.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonArrayRequest);
    }
}
