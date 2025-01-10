package com.example.garageapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.garageapp.Admin;
import com.example.garageapp.CustomerActivity;
import com.example.garageapp.R;
import com.example.garageapp.dashboard;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private static final String BASE_URL = "http://192.168.1.108/public_html/Android/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.edttext1);
        passwordEditText = findViewById(R.id.edttext2);
        loginButton = findViewById(R.id.button);
        TextView createAccountTextView = findViewById(R.id.create_account_text);
        createAccountTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Registration.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                loginUser(username, password); // Call login method
            } else {
                Toast.makeText(Login.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String username, String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LoginResponse", response); // Add this line to log the full response
                        if (response == null || response.isEmpty()) {
                            Toast.makeText(Login.this, "No response from server", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String role = jsonResponse.optString("role", "unknown");
                            Log.d("Role", role); // Log the role to see if it's coming correctly

                            if ("Admin".equalsIgnoreCase(role)) {
                                int adminId = jsonResponse.optInt("admin_id", -1);
                                int userId=jsonResponse.optInt("user_id", -1);
                                if (adminId != -1) {
                                    // Admin role, navigate to Admin activity
                                    Toast.makeText(Login.this, "Welcome, Admin", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Login.this, Admin.class);
                                    intent.putExtra("admin_id", adminId); // Pass the admin_id to AdminActivity
                                    intent.putExtra("user_id", userId);// Pass customer_id to HomeActivity
                                    intent.putExtra("role", role);
                                    startActivity(intent);
                                    finish();
                                }
                            } else if ("Customer".equalsIgnoreCase(role)) {
                                int customerId = jsonResponse.optInt("customer_id", -1);
                                int userId=jsonResponse.optInt("user_id", -1);
                                if (customerId != -1) {
                                    // Customer role, navigate to Customer activity
                                    Toast.makeText(Login.this, "Welcome, Customer", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Login.this, CustomerActivity.class);
                                    intent.putExtra("customer_id", customerId);
                                    intent.putExtra("user_id", userId);
                                    intent.putExtra("role", role);
                                    startActivity(intent);
                                    finish();
                                }
                            } else if ("Employee".equalsIgnoreCase(role)) {
                                int employeeId = jsonResponse.optInt("employee_id", -1);
                                int userId=jsonResponse.optInt("user_id", -1);
                                if (employeeId != -1) {
                                    // Save employee ID in SharedPreferences
                                    SharedPreferences sharedPreferences = getSharedPreferences("RepairApp", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("employee_id", employeeId);
                                    editor.putInt("user_id", userId);
                                    editor.putString("role", role);
                                    editor.apply();

                                    // Navigate to dashboard
                                    Intent intent = new Intent(Login.this, dashboard.class);
                                    intent.putExtra("employee_id", employeeId);
                                    intent.putExtra("user_id", userId);
                                    intent.putExtra("role", role);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(Login.this, "Role not recognized", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("LoginActivity", "JSON Parsing error: " + e.getMessage());
                            Toast.makeText(Login.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LoginActivity", "Volley Error: " + error.toString());
                        Toast.makeText(Login.this, "Error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                // Create the JSON object to send as the body
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("username", username);
                    jsonParams.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonParams.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        // Set retry policy to increase timeout
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, // Timeout in milliseconds (e.g., 10 seconds)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Default retry count
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Default backoff multiplier
        ));

        Volley.newRequestQueue(this).add(stringRequest);
    }

}