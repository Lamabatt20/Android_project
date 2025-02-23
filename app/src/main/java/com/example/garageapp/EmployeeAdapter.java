package com.example.garageapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<Employee> employeeList;
    private Context context;
    private String pathurl="http://172.19.33.18";

    // Constructor to initialize the employee list and context
    public EmployeeAdapter(Context context, List<Employee> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
    }

    public EmployeeAdapter(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for individual items in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_employees, parent, false);
        return new EmployeeViewHolder(view);
    }
    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        holder.employeeName.setText(employee.getEmployeeName());

        Log.d("EmployeeAdapter", "Binding employee ID: " + employee.getEmployeeId());

        holder.deleteButton.setOnClickListener(v -> {
            String employeeId = String.valueOf(employee.getEmployeeId());

            // Log the employee ID when the delete button is clicked
            Log.d("EmployeeAdapter", "Delete button clicked for employee ID: " + employeeId);

            // Call the delete function
            deleteEmployee(employeeId, position);
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    private void deleteEmployee(String employeeId, int position) {
        String url = pathurl + "/public_html/Android/delete_employee.php?id=" + employeeId;
        Log.d("EmployeeAdapter", "Deleting employee with ID: " + employeeId + " using URL: " + url);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest deleteRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        Log.d("EmployeeAdapter", "Response from server: " + response.toString());
                        if (response.getBoolean("success")) {
                            Log.d("EmployeeAdapter", "Employee deleted successfully");
                            employeeList.remove(position);
                            notifyItemRemoved(position);
                        } else {
                            Log.d("EmployeeAdapter", "Failed to delete employee: " + response.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("EmployeeAdapter", "Error parsing response: " + e.getMessage());
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.d("EmployeeAdapter", "Error during network request: " + error.getMessage());
                }
        );

        requestQueue.add(deleteRequest);
    }


    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView employeeName;
        ImageView deleteButton;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.employee_name);
            deleteButton = itemView.findViewById(R.id.delete);
        }
    }
}
